package com.santa.participants.models

case class Participant(
    id: String,
    sessionId: String,
    firstName: String,
    lastName: String,
    email: String,
    participates: Option[Boolean],
    comment: Option[String]
)
