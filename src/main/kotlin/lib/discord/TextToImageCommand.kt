package lib.discord

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.logging.error
import java.net.URL
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lib.openai.OpenAI
import lib.openai.TextToImageResponse
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class TextToImageCommand(
  private val openAI: OpenAI
) : Command() {

  override val name = "text2img"

  private val param = "text"

  suspend fun init(kord: Kord) {
    kord.createGlobalChatInputCommand(name, "Text to image") {
      string(param, "Text description of image") {
        required = true
      }
    }

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
      runIfNeeded(interaction)
    }
  }

  override suspend fun run(interaction: GuildChatInputCommandInteraction) {
    val deferred = interaction.deferPublicResponse()
    val text = interaction.command.strings[param]!!
    val initiator = interaction.user.mention
    val responsePrefix = "**$text** - $initiator"
    var response: TextToImageResponse? = null
    var elapsedTimeSeconds = 0
    try {
      coroutineScope {
        val state = launch {
          response = openAI.textToImage(text)
        }
        while (!state.isCompleted) {
          deferred.respond { content = "$responsePrefix ${elapsedTimeText(elapsedTimeSeconds)}" }
          delay(3000)
          elapsedTimeSeconds += 3
        }
        deferred.respond {
          content = when (val nonNullResponse = response) {
            null -> "⛔ This should not happen. Please notify @uphillsimplex UwU. ⛔"
            else -> when (nonNullResponse) {
              is TextToImageResponse.Success -> {
                addFile("$text.jpg", URL(nonNullResponse.url).openStream())
                "$responsePrefix ${elapsedTimeText(elapsedTimeSeconds)}"
              }

              is TextToImageResponse.Error -> "⛔ ${nonNullResponse.error} ⛔"
            }
          }
        }
      }
    } catch (exception: Exception) {
      deferred.respond {
        content = when (exception) {
          is HttpRequestTimeoutException -> "⛔ Request timed out after 60 secs ⛔"
          else -> "⛔ ${exception.message} ⛔"
        }
      }
      log.error(exception)
    }
  }

  private fun elapsedTimeText(elapsedTimeSeconds: Int) = "(elapsed time: $elapsedTimeSeconds secs)"
}
