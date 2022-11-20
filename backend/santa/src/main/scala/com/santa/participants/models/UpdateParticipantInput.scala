package com.santa.participants.models

case class UpdateParticipantInput(
     firstName: Option[String],
     lastName: Option[String],
     email: Option[String],
     participates: Option[Boolean],
     comment: Option[String]
)
