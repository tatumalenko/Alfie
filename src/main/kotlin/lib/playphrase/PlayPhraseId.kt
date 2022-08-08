package lib.playphrase

import io.ktor.http.encodeURLQueryComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PlayPhraseId(
  val phrase: String,
  var index: Int = 0
) {
  private val endpoint = "https://www.playphrase.me/api/v1"

  fun url() = "$endpoint/phrases/search?q=${phrase.encodeURLQueryComponent()}&limit=10&platform=desktop%20safari&skip=0"

  fun next() = copy(index + 1)

  fun back() = copy(maxOf(0, index - 1))

  fun encodeToString() = Json.encodeToString(this)

  private fun copy(index: Int) = PlayPhraseId(phrase, index)
}
