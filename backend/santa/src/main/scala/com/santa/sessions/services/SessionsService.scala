package com.santa.sessions.services

import cats.effect.IO
import com.santa.matches.models.{CreateMatchInput, Match}
import com.santa.matches.services.MatchesService
import com.santa.participants.services.ParticipantsService
import com.santa.sessions.models.{CreateSessionInput, Session, SessionNotFoundError, UpdateSessionInput}
import com.santa.sessions.repositories.SessionsRepository
import cats.implicits._
import com.santa.config.config.MailConfig
import com.santa.emails.{EmailRequest, EmailSendResponse, EmailsService, Recipient, RecipientPersonalization}

import java.util.UUID
import scala.collection.immutable.List
import scala.util.Random

trait SessionsService {

  def create(input: CreateSessionInput): IO[Session]

  def getSessions: IO[List[Session]]

  def get(sessionId: String): IO[Either[SessionNotFoundError.type, Session]]

  def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]]

  def updateSession(id: String, updateInput: UpdateSessionInput): IO[Either[SessionNotFoundError.type, Session]]

  def scramble(id: String): IO[List[Match]]

  def launch(id: String): IO[EmailSendResponse]

  def sendOutcomes(id: String): IO[EmailSendResponse]
}

class SessionsServiceImpl(
  mailConfig: MailConfig,
  sessionRepository: SessionsRepository,
  participantsService: ParticipantsService,
  matchesService: MatchesService,
  emailsService: EmailsService
) extends SessionsService {

  override def create(input: CreateSessionInput): IO[Session] = {
    sessionRepository.create(Session(
      id = UUID.randomUUID().toString,
      name = input.name,
      passphrase = input.passphrase,
      sessionScrambled = false, emailsSent = false
    ))
  }

  override def getSessions: IO[List[Session]] = {
    sessionRepository.getSessions
  }

  override def get(id: String): IO[Either[SessionNotFoundError.type, Session]] = {
    sessionRepository.get(id)
  }

  override def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]] = {
    sessionRepository.deleteSession(id)
  }

  override def updateSession(id: String, updateInput: UpdateSessionInput): IO[Either[SessionNotFoundError.type, Session]] = {
    sessionRepository.get(id).flatMap({
      case Right(a) => sessionRepository.updateSession(id, a.copy(
        name = updateInput.name.getOrElse(a.name),
        sessionScrambled = updateInput.sessionScrambled.getOrElse(a.sessionScrambled),
        emailsSent = updateInput.emailsSent.getOrElse(a.emailsSent)
      ))
      case Left(_) => IO(Left(SessionNotFoundError))
    })
  }


  override def scramble(id: String): IO[List[Match]] = {
    for {
      participants <- participantsService.getParticipants(id)
      randomizedList = Random.shuffle(participants).filter(_.participates.getOrElse(false))
      matchInputs = randomizedList.zip(randomizedList.drop(1) ++ List(randomizedList.head)).map(tuple => {
        CreateMatchInput(id, tuple._1.id, tuple._2.id)
      })
      matches <- matchInputs.map(matchInput => matchesService.create(matchInput)).sequence
      _ <- updateSession(id, UpdateSessionInput(None, sessionScrambled = Some(true), None))
    } yield {
      matches
    }
  }

  override def launch(id: String): IO[EmailSendResponse] = {
    for {
      session <- get(id).map(_.getOrElse(throw new Exception(s"Could not find session with id ${id}")))
      participants <- participantsService.getParticipants(id).map(_.filter(_.participates.getOrElse(false)))
      emailResponse <- participants.map(participant => {
        emailsService.sendRequest(EmailRequest(
          from = Recipient("Shush Santa", "santa@shush-santa.ch"),
          to = List(Recipient(participant.name, participant.email)),
          personalization = List(RecipientPersonalization(
            email = participant.email,
            data = Map("name" -> participant.name,
              "session_name" -> session.name,
              "participantId" -> participant.id,
            ))),
          template_id = mailConfig.invitationTemplate
        ))
      }).sequence
      _ <- updateSession(id, UpdateSessionInput(None, None, emailsSent = Some(true)))
    } yield {
      emailResponse.head
    }
  }

  override def sendOutcomes(id: String): IO[EmailSendResponse] = {
    for {
      participants <- participantsService.getParticipants(id)
      matches <- matchesService.getMatches(id)
      emailResponse <- participants.map(participant => {
        val matching = matches.find(_.giver == participant.id).getOrElse(throw new Exception(s"No matching found for participant ${participant.id}"))
        val targetParticipant = participants.find(_.id == matching.receiver).getOrElse(throw new Exception(s"Could not find receiver in match ${matching.id}"))
        emailsService.sendRequest(EmailRequest(
          from = Recipient("Shush Santa", "santa@shush-santa.ch"),
          to = List(Recipient(participant.name, participant.email)),
          personalization = List(RecipientPersonalization(
            email = participant.email,
            data = Map("name" -> participant.name,
              "target_name" -> targetParticipant.name,
              "target_comment" -> targetParticipant.comment.getOrElse("No preference!"),
            ))),
          template_id = mailConfig.outcomeTemplate
        ))
      }).sequence
    } yield {
      emailResponse.head
    }
  }
}
