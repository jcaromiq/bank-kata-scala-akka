package com.bankkata

import akka.actor.{Actor, Props}
import com.bankkata.PrinterActor.{DisplayMessage, InsertPinMessage, WelcomeMessage}

object PrinterActor {
  def props: Props = Props(new PrinterActor)

  case class DisplayMessage(message: String)
  case class InsertPinMessage()
  case class WelcomeMessage()
}

class PrinterActor() extends Actor {
  override def receive: Receive = {
    case InsertPinMessage() => println("Insert card please")
    case DisplayMessage(msg) => println(msg)
    case WelcomeMessage() => println("pin ok")
  }
}