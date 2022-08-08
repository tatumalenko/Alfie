package lib.playphrase

import csrfAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import lib.aws.AppConfig

class PlayPhrase(
  private val client: HttpClient,
  private val config: AppConfig
) {
  suspend fun search(phraseId: PlayPhraseId) = client.get(phraseId.url()) {
    csrfAuth(config.playPhrase.token)
  }.body<SearchResult>().phrases
}
