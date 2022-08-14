package lib.aws

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIConfig(
  val token: String,
  val timeoutMilliseconds: Long
)
