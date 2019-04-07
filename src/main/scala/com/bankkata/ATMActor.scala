package com.bankkata

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.bankkata.ATMActor.{InsertCard, InsertPin}
import com.bankkata.PrinterActor.{DepositSuccess, InsertPinMessage, InvalidPinMessage, WelcomeMessage}

object ATMActor {
  def props(printer: ActorRef): Props = Props(new ATMActor(printer))

  case class InsertCard(cardNumber: String)
  case class InsertPin(pinNumber: String)

}
class ATMActor(printer: ActorRef) extends Actor with ActorLogging {
  var card: String = _
  var accountRef: ActorRef = _

  override def receive: Receive = {
    case InsertCard(insertedCard) =>
      this.card = insertedCard
      printer ! InsertPinMessage()
      context become WithCardInserted()
  }

  def WithCardInserted(): Receive = {
    case InsertPin(pin) =>
      if (card.substring(0, 3).equals(pin)) {
        printer ! WelcomeMessage()
        accountRef = context.actorOf(Props[AccountActor])
        context become WithAuthenticated()
      } else {
        printer ! InvalidPinMessage()
      }
  }

  def WithAuthenticated(): Receive = {
    case Deposit(amount) =>
      accountRef ! Deposit(amount)
      printer ! DepositSuccess()
    case Withdraw(amount) =>
      accountRef ! Withdraw(amount)
  }

}
