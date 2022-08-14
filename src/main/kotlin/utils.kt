import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters

fun HttpMessageBuilder.csrfAuth(token: String) {
  header(HttpHeaders.Authorization, "Token")
  header("X-Csrf-Token", token)
}

class FormDataContentBuilder(
  var params: MutableList<Pair<String, String>> = mutableListOf()
) {
  fun append(key: String, value: String) {
    params.add(Pair(key, value))
  }
}

fun HttpRequestBuilder.formDataContent(block: FormDataContentBuilder.() -> Unit) {
  val formDataContent = FormDataContentBuilder().apply(block)
  setBody(
    FormDataContent(
      Parameters.build {
        formDataContent.params.forEach { append(it.first, it.second) }
      }
    )
  )
}
