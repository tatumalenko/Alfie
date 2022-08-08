package lib.discord

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.toByteArray
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import lib.dedupe.ImageHasher
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class Dedupe(
  private val client: HttpClient,
  private val imageHasher: ImageHasher
) {
  suspend fun init(kord: Kord) {
    kord.on<MessageCreateEvent> {
      log.info("Message intercepted")
      message.data.attachments.firstOrNull()?.let {
        val payload = client.get(it.url)
        val img = ImageIO.read(ByteArrayInputStream(payload.bodyAsChannel().toByteArray()))
        val imgHash = imageHasher.hash(img)
        // TODO: query DDB to find existing primary key -> if so, img is a dup
      }
    }
  }
}
