package lib.playphrase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Phrase(
  @SerialName("video-url") val videoUrl: String,
  val text: String
)
