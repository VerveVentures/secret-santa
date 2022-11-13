package com.santa.emails

import com.santa.participants.models.Participant
import com.mailersend.sdk.{MailerSend, MailerSendResponse, Recipient}
import com.mailersend.sdk.emails.Email
import com.mailersend.sdk.exceptions.MailerSendException
import com.santa.config.config.MailConfig
import com.santa.sessions.models.Session


class EmailsService(mailConfig: MailConfig) {

  def sendInvitationEmail(session: Session, participant: Participant): Boolean = {
    val email = new Email
    email.setFrom("Shush Santa", "santa@shush-santa.ch")
    val recipient = new Recipient(participant.name, participant.email)
    email.recipients.add(recipient)

    email.setTemplateId(mailConfig.invitationTemplate)

    email.addPersonalization(recipient, "name", participant.name)

    email.addPersonalization(recipient, "session_name", session.name)
    email.addPersonalization(recipient, "participantId", participant.id)

    val ms = new MailerSend

    ms.setToken(mailConfig.apiToken)
    try {
      val response = ms.emails().send(email)
      println(response.messageId)
      true
    } catch {
      case e: Exception =>
        e.printStackTrace
        false
    }
  }
}
