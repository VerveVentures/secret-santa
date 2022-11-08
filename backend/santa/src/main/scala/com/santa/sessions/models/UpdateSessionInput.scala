package com.santa.sessions.models

case class UpdateSessionInput(
  name: Option[String],
  sessionScrambled: Option[Boolean],
  emailsSent: Option[Boolean]
)
