package lib.openai

import formDataContent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import lib.aws.OpenAIConfig

class OpenAI(
  private val client: HttpClient,
  private val config: OpenAIConfig
) {
  private val endpoint = "https://api.deepai.org/api/text2img"

  suspend fun textToImage(prompt: String): TextToImageResponse =
    client.post(endpoint) {
      header("api-key", config.token)
      formDataContent {
        append("text", prompt)
      }
      timeout {
        requestTimeoutMillis = config.timeoutMilliseconds
      }
    }.body()
}
