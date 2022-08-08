package model.urbandictionary

import kotlinx.serialization.Serializable

@Serializable
data class Definition(
  val definition: String,
  val permalink: String,
  val example: String
)
