import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

suspend fun main(args: Array<String>) {
  val kord = Kord(System.getenv("DISCORD_TOKEN"))

  Define.init(kord)

  kord.on<GuildChatInputCommandInteractionCreateEvent> {
    when (interaction.command.rootName) {
      Define.name -> Define.run(interaction)
    }
  }

  kord.login {
    @OptIn(PrivilegedIntent::class)
    intents += Intent.MessageContent
  }
}
