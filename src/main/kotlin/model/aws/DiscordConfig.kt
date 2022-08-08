package model.aws

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfig(
  @SerialName("bot-token") val botToken: String,
  @SerialName("guild-id") val guildId: String,
  @SerialName("dups-channel-id") val dupsChannelId: String,
  @SerialName("uniqs-channel-id") val uniqsChannelId: String
)
