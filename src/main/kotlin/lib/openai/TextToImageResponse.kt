package lib.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = TextToImageResponse.Serializer::class)
sealed class TextToImageResponse {
  @Serializable
  data class Success(
    val id: String,
    @SerialName("output_url") val url: String
  ) : TextToImageResponse()

  @Serializable
  data class Error(
    @SerialName("err") val error: String
  ) : TextToImageResponse()

  object Serializer : JsonContentPolymorphicSerializer<TextToImageResponse>(TextToImageResponse::class) {
    override fun selectDeserializer(element: JsonElement) = when {
      "err" in element.jsonObject -> Error.serializer()
      else -> Success.serializer()
    }
  }
}
