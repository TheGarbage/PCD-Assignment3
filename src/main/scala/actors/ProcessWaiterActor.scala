package actors

import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import resources.View

object ProcessWaiterActor {
  def apply(startTime: Long, view: View, rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Behavior[String] = Behaviors.receive[String]{ (_, _) =>
    view.setFinish(" Stopped")
    Behaviors.stopped
  }.receiveSignal({ case (_, Terminated(_)) =>
    view.setFinish(" Time to finish: " + (System.currentTimeMillis() - startTime) + "ms")
    rankingActor ! None
    countersActor ! None
    TerminationWaiterActor()
  })
}