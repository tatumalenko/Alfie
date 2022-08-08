package lib.playphrase

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
  val count: Int,
  val phrases: List<Phrase>
)
