package com.santa.matches.models

case class Match(
  id: String,
  sessionId: String,
  giver: String,
  receiver: String
)
