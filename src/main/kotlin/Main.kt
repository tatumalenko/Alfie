import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.toByteArray
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

suspend fun main(args: Array<String>) {
  val kord = Kord(System.getenv("DISCORD_TOKEN"))

  Define.init(kord)

  kord.on<MessageCreateEvent> {
    message.data.attachments.firstOrNull()?.let {
      val payload = httpClient.get(it.url)
      val img = ImageIO.read(ByteArrayInputStream(payload.bodyAsChannel().toByteArray()))
      val imgHash = ImageHasher.hash(img)
      // TODO: query DDB to find existing primary key -> if so, img is a dup
    }
  }

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
