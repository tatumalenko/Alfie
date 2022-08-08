import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import java.io.File
import java.net.URI
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import lib.dedupe.ImageHasher
import lib.playphrase.SearchResult
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImageHasherTest {
  private val httpClient = HttpClient(CIO) {
    install(ContentEncoding) {
      deflate(1.0F)
      gzip(0.9F)
    }
    install(ContentNegotiation) {
      json(Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
  }
  private val imageHasher = ImageHasher(httpClient)
  private val util = Util()

  @Test
  fun `all different images`() {
    val dir = File("src/test/resources/different")
    val files = dir.listFiles()!!
    val pairs = files.flatMap { f0 -> files.map { f1 -> if (f0 !== f1) Pair(f0, f1) else null } }.filterNotNull()
    pairs.forEach { (f0, f1) ->
      assertFalse(imageHasher.similar(f0, f1), "$f0, $f1")
    }
  }

  @Test
  fun `exactly same image`() {
    assertTrue(imageHasher.similar("fbi.png", "fbi.png"))
  }

  @Test
  fun `slightly different image`() {
    assertTrue(imageHasher.similar("fbi.png", "fbi-camera-roll.png"))
  }

  @Test
  fun `cropped image`() {
    assertTrue(imageHasher.similar("tattoo.jpg", "tattoo-cropped.jpg"))
  }

  @Test
  fun `heavily edited image`() {
    assertTrue(imageHasher.similar("fbi.png", "fbi-heavily-edited.jpg"))
  }

  @Test
  fun `different image`() {
    assertFalse(imageHasher.similar("fbi.png", "wings.jpg"))
  }

  @Test
  fun `uniqs`(): Unit = runBlocking {
    val messages = util.uniqs()
    val uris = messages
      .filter { it.attachments.size == 2 }
      .map {
        val (first, second) = it.attachments.toList()
        Pair(URI(first.url), URI(second.url))
      }
    uris.collect {
      assertFalse(
        imageHasher.similar(it.first, it.second),
        "first: ${it.first}\nsecond: ${it.second}"
      )
    }
  }

  @Test
  fun `dups`(): Unit = runBlocking {
    val messages = util.dups()
    val uris = messages
      .filter { it.attachments.size == 2 }
      .map {
        val (first, second) = it.attachments.toList()
        Pair(URI(first.url), URI(second.url))
      }
    uris.collect {
      assertTrue(
        imageHasher.similar(it.first, it.second),
        "first: ${it.first}\nsecond: ${it.second}"
      )
    }
  }

  @Test
  fun test() = runBlocking {
    val response =
      httpClient.get("https://www.playphrase.me/api/v1/phrases/search?q=tatum&limit=10&platform=desktop%20safari&skip=0") {
        csrfAuth("cmf6ALYjeK3Xxi1Wobc1dIitdPqz+IjROylUqKHePZ+HQCkfROzIedaKmgSWlbgJogBBpd5HpkcmvFLF")
      }
    val body = response.body<SearchResult>()
    println(body)
  }
}
