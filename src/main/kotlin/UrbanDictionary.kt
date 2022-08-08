import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import model.urbandictionary.Definition
import model.urbandictionary.SearchResult

class UrbanDictionary(
  private val httpClient: HttpClient
) {
  private val endpoint = "https://api.urbandictionary.com/v0"

  private val url = { term: String -> "$endpoint/define?term=${URLEncoder.encode(term, UTF_8)}" }

  suspend fun search(term: String): List<Definition> {
    return httpClient.get(url(term)).body<SearchResult>().list
  }
}
