package com.bankkata

import akka.actor.Actor

case class Deposit(amount:Int)

class AccountActor extends Actor{
  var balance: Int = 0

  override def receive: Receive = {
    case Deposit(amount) => balance += amount
  }
}
