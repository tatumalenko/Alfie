package lib.discord

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import lib.inspire.Inspire
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class InspireCommand(
  private val inspire: Inspire
) : Command() {

  override val name = "inspire"

  suspend fun init(kord: Kord) {
    kord.createGlobalChatInputCommand(name, "Inspire search")

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
      runIfNeeded(interaction)
    }
  }

  override suspend fun run(interaction: GuildChatInputCommandInteraction) {
    val deferred = interaction.deferPublicResponse()
    try {
      deferred.respond {
        content = inspire.search().toString()
      }
    } catch (exception: Exception) {
      log.error { exception }
    }
  }
}
