package actors

import akka.actor.typed.{Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import resources.View

object ProcessWaiter {
  def apply(startTime: Long, view: View): Behavior[String] = Behaviors.receive[String]{ (_, _) =>
    view.setFinish(" Stopped")
    Behaviors.stopped
  }.receiveSignal({ case (_, Terminated(_)) =>
    view.setFinish(" Time to finish: " + (System.currentTimeMillis() - startTime) + "ms")
    Behaviors.stopped
  })
}