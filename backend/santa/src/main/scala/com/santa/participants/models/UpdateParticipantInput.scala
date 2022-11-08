package com.santa.participants.models

case class UpdateParticipantInput(
     name: Option[String],
     email: Option[String],
     participates: Option[Boolean],
     comment: Option[String]
)
