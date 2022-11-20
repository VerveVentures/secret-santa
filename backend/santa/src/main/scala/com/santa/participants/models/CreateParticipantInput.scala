package com.santa.participants.models

case class CreateParticipantInput(
   sessionId: String,
   firstName: String,
   lastName: String,
   email: String,
   participates: Option[Boolean],
   comment: Option[String]
)
