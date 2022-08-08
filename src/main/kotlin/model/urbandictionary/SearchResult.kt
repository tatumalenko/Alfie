package model.urbandictionary

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
  val list: List<Definition>
)
