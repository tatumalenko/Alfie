package model.aws

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
  val discord: DiscordConfig
)
