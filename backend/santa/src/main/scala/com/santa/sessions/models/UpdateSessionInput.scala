package com.santa.sessions.models

case class UpdateSessionInput(
  name: String,
  sessionScrambled: Boolean,
  emailsSent: Boolean
)
