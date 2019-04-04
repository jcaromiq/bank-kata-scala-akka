package com.bankata

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import com.bankkata.{AccountActor, Deposit, Withdraw}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class BankAccountSpec(_system: ActorSystem)
    extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("BankAccountSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "An Account Actor" should {

    "deposit amount in a Bank Account" in {
      val actor = TestActorRef[AccountActor]

      actor ! Deposit(100)

      assert(actor.underlyingActor.balance.equals(100))
    }

    "withdraw amount in a Bank Account" in {
      val actor = TestActorRef[AccountActor]

      actor ! Deposit(1000)
      actor ! Withdraw(100)

      assert(actor.underlyingActor.balance.equals(900))
    }
  }
}
