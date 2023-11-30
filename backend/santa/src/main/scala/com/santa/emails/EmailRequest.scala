package com.santa.emails

case class Recipient(name: String, email: String)
case class RecipientPersonalization(email: String, data: Map[String, String])
case class EmailRequest(from: Recipient, to: List[Recipient], personalization: List[RecipientPersonalization], template_id: String)
case class EmailSendResponse(status: Int)
