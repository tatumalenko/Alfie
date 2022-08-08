import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

fun HttpMessageBuilder.csrfAuth(token: String) {
  header(HttpHeaders.Authorization, "Token")
  header("X-Csrf-Token", token)
}
