package com.bankkata

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.bankkata.ATMActor.{InsertCard, InsertPin}
import com.bankkata.PrinterActor.{InsertPinMessage, InvalidPinMessage, WelcomeMessage}

object ATMActor {
  def props(printer: ActorRef): Props = Props(new ATMActor(printer))

  case class InsertCard(cardNumber: String)
  case class InsertPin(pinNumber: String)

}
class ATMActor(printer: ActorRef) extends Actor with ActorLogging {
  var card: String = _

  override def receive: Receive = {
    case InsertCard(insertedCard) =>
      this.card = insertedCard
      printer ! InsertPinMessage()
      context become WithCardInserted()
  }

  def WithCardInserted(): Receive = {
    case InsertPin(pin) =>
      if(card.substring(0,3).equals(pin)) {
        printer ! WelcomeMessage()
        context become WithCardInserted()
      } else {
        printer ! InvalidPinMessage()
      }

  }

}
