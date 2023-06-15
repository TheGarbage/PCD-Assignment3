package es1.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, Terminated}

import java.io.File

object WaiterActor {
  def apply(count: Int): Behavior[File] = Behaviors.receiveSignal { case (_, Terminated(_)) =>
    count match {
      case 1 => Behaviors.stopped
      case c => WaiterActor(c - 1)
    }
  }
}