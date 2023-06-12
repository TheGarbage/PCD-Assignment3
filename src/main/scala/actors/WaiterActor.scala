package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, Terminated}

import java.io.File

object WaiterActor {
  def apply(count: Int = 0): Behavior[File] = Behaviors.receiveSignal[File]{ case (_, Terminated(_)) =>
    count match {
      case 1 => Behaviors.stopped
      case c => WaiterActor(c - 1)
    }
  }
}