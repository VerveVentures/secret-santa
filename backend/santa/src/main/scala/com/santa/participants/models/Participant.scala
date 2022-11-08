package com.santa.participants.models

case class Participant(
    id: String,
    sessionId: String,
    name: String,
    email: String,
    participates: Option[Boolean],
    comment: Option[String]
)
