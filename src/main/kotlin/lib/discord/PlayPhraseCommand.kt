package lib.discord

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.actionRow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import lib.playphrase.Phrase
import lib.playphrase.PlayPhrase
import lib.playphrase.PlayPhraseId
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class PlayPhraseCommand(
  private val playPhrase: PlayPhrase
) : Command() {
  override val name = "pp"

  private val param = "phrase"

  suspend fun init(kord: Kord) {
    kord.createGlobalChatInputCommand(name, "PlayPhrase") {
      string(param, "Phrase to search") {
        required = true
      }
    }

    kord.on<GuildButtonInteractionCreateEvent> {
      val deferred = interaction.deferPublicMessageUpdate()
      val phraseId = Json.decodeFromString<PlayPhraseId>(interaction.componentId)
      val phrases = playPhrase.search(phraseId)
      deferred.edit {
        show(phrases, phraseId)
      }
    }

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
      runIfNeeded(interaction)
    }
  }

  override suspend fun run(interaction: GuildChatInputCommandInteraction) {
    val deferred = interaction.deferPublicResponse()
    val command = interaction.command
    val phraseId = PlayPhraseId(command.strings[param]!!)
    try {
      val phrases = playPhrase.search(phraseId)
      deferred.respond {
        show(phrases, phraseId)
      }
    } catch (exception: Exception) {
      log.error { exception }
    }
  }

  private fun InteractionResponseModifyBuilder.show(
    phrases: List<Phrase>,
    phrase: PlayPhraseId
  ) {
    content = phrases.getOrNull(phrase.index)?.videoUrl ?: "Nothing found."
    actionRow {
      if (phrase.index != 0) {
        interactionButton(ButtonStyle.Primary, phrase.back().encodeToString()) {
          label = "⇤"
        }
      }
      if (phrase.index < 4) {
        interactionButton(ButtonStyle.Primary, phrase.next().encodeToString()) {
          label = "⇥"
        }
      }
    }
  }
}
