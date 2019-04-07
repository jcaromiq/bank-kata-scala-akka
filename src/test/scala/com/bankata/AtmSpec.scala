package com.bankata

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import com.bankkata.ATMActor.{InsertCard, InsertPin}
import com.bankkata.PrinterActor.{DisplayMessage, InsertPinMessage, WelcomeMessage}
import com.bankkata.{ATMActor, AccountActor, Deposit, PrinterActor, Withdraw}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Matchers, WordSpecLike}

class AtmSpec(_system: ActorSystem)
    extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll
    with BeforeAndAfter {

  var printer: TestProbe = _
  var atm: ActorRef = _

  def this() = this(ActorSystem("ATMSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  before {
    printer = TestProbe()
    atm = system.actorOf(ATMActor.props(printer.ref))
  }

  "An ATM" should {
    "ask for pin given a card" in {
      atm ! InsertCard("1234567")

      val msg = printer.fishForSpecificMessage() {
        case msg @ InsertPinMessage() => msg
      }
      msg should be(InsertPinMessage())
    }
    "display welcome message when pin is ok" in {
      atm ! InsertCard("1234567")

      atm ! InsertPin("123")

      val msg = printer.fishForSpecificMessage() {
        case msg @ WelcomeMessage() => msg
      }
      msg should be(WelcomeMessage())
    }

  }
}
