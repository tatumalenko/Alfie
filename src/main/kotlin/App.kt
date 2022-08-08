import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.ktor.client.HttpClient
import mu.KotlinLogging
import org.apache.log4j.BasicConfigurator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val log = KotlinLogging.logger {}

class Main : KoinComponent {
  private val app: App by inject()

  suspend fun run() {
    app.start()
  }
}

class App(
  private val httpClient: HttpClient,
  private val kord: Kord,
  private val define: Define,
  private val playPhrase: PlayPhrase,
  private val imageHasher: ImageHasher
) {
  suspend fun start() {
    BasicConfigurator.configure()

    log.info("Starting App")

    define.init(kord)
    playPhrase.init(kord)

//    kord.on<MessageCreateEvent> {
//      log.info("Message intercepted")
//      message.data.attachments.firstOrNull()?.let {
//        val payload = httpClient.get(it.url)
//        val img = ImageIO.read(ByteArrayInputStream(payload.bodyAsChannel().toByteArray()))
//        val imgHash = imageHasher.hash(img)
//        // TODO: query DDB to find existing primary key -> if so, img is a dup
//      }
//    }

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
      when (interaction.command.rootName) {
        Define.name -> define.run(interaction)
        PlayPhrase.name -> playPhrase.run(interaction)
      }
    }

    kord.login {
      @OptIn(PrivilegedIntent::class)
      intents += Intent.MessageContent
    }
  }
}
