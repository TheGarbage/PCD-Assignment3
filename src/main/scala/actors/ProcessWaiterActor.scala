package actors

import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, Ranking, View}

object ProcessWaiterActor {
  def apply(startTime: Long, renderActor: ActorRef[(Option[Ranking],Option[Counters], Option[String])], rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Behavior[String] = Behaviors.receive[String]{ (_, _) =>
    renderActor ! (None, None, Some(" Stopped"))
    Behaviors.stopped
  }.receiveSignal({ case (_, Terminated(_)) =>
    renderActor ! (None, None, Some(" Time to finish: " + (System.currentTimeMillis() - startTime) + "ms"))
    rankingActor ! None
    countersActor ! None
    TerminationWaiterActor()
  })
}