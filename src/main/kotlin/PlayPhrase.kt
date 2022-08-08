import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.string
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class PlayPhrase(
  private val client: HttpClient
) {
  companion object {
    const val name = "pp"
  }

  private val param = "phrase"

  private val regex = Regex("(https://.*\\.mp4)")

  suspend fun init(kord: Kord) {
    kord.createGlobalChatInputCommand(name, "PlayPhrase") {
      string(param, "Phrase to search") {
        required = true
      }
    }
  }

  suspend fun run(interaction: GuildChatInputCommandInteraction) {
    val deferred = interaction.deferPublicResponse()
    val command = interaction.command
    val phrase = command.strings[param]!!
    try {
      val response =
        client.get("https://www.playphrase.me/#/search?q=${URLEncoder.encode(phrase, StandardCharsets.UTF_8)}")
      val url = response.bodyAsText()
        .lines()
        .find { regex.containsMatchIn(it) }
        ?.let { regex.find(it)?.groupValues?.firstOrNull() }
      deferred.respond {
        content = url ?: "Nothing found."
      }
    } catch (exception: Exception) {
      println(exception)
    }
  }
}
