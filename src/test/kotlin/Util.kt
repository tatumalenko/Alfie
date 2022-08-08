import aws.sdk.kotlin.services.appconfigdata.AppConfigDataClient
import aws.sdk.kotlin.services.appconfigdata.model.GetLatestConfigurationRequest
import aws.sdk.kotlin.services.appconfigdata.model.StartConfigurationSessionRequest
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.TextChannel
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.aws.AppConfig

suspend fun appConfig(): AppConfig? {
  val appConfigDataClient = AppConfigDataClient { region = "us-east-1" }
  val startConfigSessionResponse = appConfigDataClient.startConfigurationSession(StartConfigurationSessionRequest {
    applicationIdentifier = "olwvmhe"
    environmentIdentifier = "cohtsbb"
    configurationProfileIdentifier = "2zz1ros"
  })
  val latestConfigResponse = appConfigDataClient.getLatestConfiguration(
    GetLatestConfigurationRequest {
      configurationToken = startConfigSessionResponse.initialConfigurationToken
    }
  )
  val config =
    latestConfigResponse.configuration?.toString(StandardCharsets.UTF_8)
      ?.let { Json.decodeFromString<AppConfig>(it) }
  return config
}

class Util {
  private val appConfig by lazy {
    runBlocking { appConfig() }
  }
  private val kord by lazy {
    runBlocking { Kord(System.getenv("DISCORD_TOKEN")) }
  }

  suspend fun uniqs(): Flow<Message> {
    val guild = kord.getGuild(Snowflake(appConfig!!.discord.guildId))
    val channel = guild!!.getChannel(Snowflake(appConfig!!.discord.uniqsChannelId)) as TextChannel
    return channel.messages
  }

  suspend fun dups(): Flow<Message> {
    val guild = kord.getGuild(Snowflake(appConfig!!.discord.guildId))
    val channel = guild!!.getChannel(Snowflake(appConfig!!.discord.dupsChannelId)) as TextChannel
    return channel.messages
  }
}
