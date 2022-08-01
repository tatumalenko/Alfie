import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Definition(
  val definition: String,
  val permalink: String,
  val example: String
)

@Serializable
data class SearchResult(
  val list: List<Definition>
)

object UrbanDictionary {
  private const val API_ENDPOINT = "https://api.urbandictionary.com/v0"

  private val url = { term: String -> "$API_ENDPOINT/define?term=${URLEncoder.encode(term, UTF_8)}" }

  private val client by lazy {
    HttpClient(CIO) {
      install(ContentNegotiation) {
        json(Json {
          prettyPrint = true
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
    }
  }

  suspend fun search(term: String): List<Definition> {
    return client.get(url(term)).body<SearchResult>().list
  }
}
