package lib.discord

import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction

abstract class Command {
  protected abstract val name: String

  protected abstract suspend fun run(interaction: GuildChatInputCommandInteraction)

  suspend fun runIfNeeded(interaction: GuildChatInputCommandInteraction) {
    if (interaction.command.rootName == name) {
      run(interaction)
    }
  }
}
