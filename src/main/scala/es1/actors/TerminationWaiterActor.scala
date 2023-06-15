package es1.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, Terminated}

import java.io.File

object TerminationWaiterActor {
  def apply(count: Int = 2): Behavior[String] = Behaviors.receiveSignal{ case (_, Terminated(_)) =>
    count match {
      case 1 => Behaviors.stopped
      case c => TerminationWaiterActor(c - 1)
    }
  }
}