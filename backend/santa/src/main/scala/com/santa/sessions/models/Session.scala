package com.santa.sessions.models

case class Session(
  name: String,
  id: String,
  passphrase: String,
  sessionScrambled: Boolean,
  emailsSent: Boolean
)
