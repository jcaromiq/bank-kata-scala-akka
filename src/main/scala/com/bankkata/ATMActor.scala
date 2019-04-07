package com.bankkata

import akka.actor.{Actor, ActorRef, Props}
import com.bankkata.ATMActor.{InsertCard, InsertPin}
import com.bankkata.PrinterActor.{InsertPinMessage, WelcomeMessage}

object ATMActor {
  def props(printer: ActorRef): Props = Props(new ATMActor(printer))

  case class InsertCard(cardNumber: String)
  case class InsertPin(pinNumber: String)

}
class ATMActor(printer: ActorRef) extends Actor {
  override def receive: Receive = {
    case InsertCard(card) => printer ! InsertPinMessage()
    case InsertPin(pin) => printer ! WelcomeMessage()
  }
}


