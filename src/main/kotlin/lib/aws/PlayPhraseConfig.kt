package lib.aws

import kotlinx.serialization.Serializable

@Serializable
data class PlayPhraseConfig(
  val token: String
)
