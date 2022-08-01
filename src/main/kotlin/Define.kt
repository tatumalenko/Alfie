import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.embed

object Define {
  const val name = "def"
  private const val param = "term"

  suspend fun init(kord: Kord) {
    kord.createGlobalChatInputCommand(name, "Urban dictionary search") {
      string(param, "Term to search") {
        required = true
      }
    }
  }

  suspend fun run(interaction: GuildChatInputCommandInteraction) {
    val deferred = interaction.deferPublicResponse()
    val command = interaction.command
    val term = command.strings[param]!!
    try {
      val definitions = UrbanDictionary.search(term)
      deferred.respond {
        if (definitions.isEmpty()) {
          content = "No results found for '$term'."
          return@respond
        }

        val first = definitions.first()
        embed {
          title = "Definition of '$term'"
          description = """
            ${first.definition}
            
            Example:
            ${first.example}
          """.trimIndent()
          url = first.permalink
          color = Color((0..255).random())
        }
      }
    } catch (exception: Exception) {
      println(exception)
    }
  }
}
