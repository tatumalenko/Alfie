package lib.urbandictionary

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLQueryComponent

class UrbanDictionary(
  private val httpClient: HttpClient
) {
  private val endpoint = "https://api.urbandictionary.com/v0"

  private fun url(term: String) = "$endpoint/define?term=${term.encodeURLQueryComponent()}"

  suspend fun search(term: String): List<Definition> {
    return httpClient.get(url(term)).body<SearchResult>().list
  }
}
