package com.santa.participants.models

case class UpdateParticipantInput(
     name: String,
     email: String,
     participates: Boolean,
     comment: String
)
