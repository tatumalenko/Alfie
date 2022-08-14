package lib.aws

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
  val discord: DiscordConfig,
  val playPhrase: PlayPhraseConfig,
  val openAI: OpenAIConfig
)
