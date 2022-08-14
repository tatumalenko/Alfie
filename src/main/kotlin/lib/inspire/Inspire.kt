package lib.inspire

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.net.URI

class Inspire(
  private val client: HttpClient
) {
  suspend fun search(): URI {
    val response = client.get("https://inspirobot.me/api?generate=true")
    return URI(response.bodyAsText())
  }
}
