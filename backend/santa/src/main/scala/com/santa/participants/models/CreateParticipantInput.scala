package com.santa.participants.models

case class CreateParticipantInput(
   sessionId: String,
   name: String,
   email: String,
   participates: Option[Boolean],
   comment: Option[String]
)
