package com.santa.sessions.models

case class Session(
  id: String,
  name: String,
  passphrase: String,
  sessionScrambled: Boolean,
  emailsSent: Boolean
)
