import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import lib.discord.DefineCommand
import lib.discord.PlayPhraseCommand
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
  private val kord: Kord,
  private val defineCommand: DefineCommand,
  private val playPhraseCommand: PlayPhraseCommand
) {
  suspend fun start() {
    BasicConfigurator.configure()

    log.info("Starting App")

    defineCommand.init(kord)
    playPhraseCommand.init(kord)

    kord.login {
      @OptIn(PrivilegedIntent::class)
      intents += Intent.MessageContent
    }
  }
}
