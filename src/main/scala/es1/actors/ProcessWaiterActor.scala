package es1.actors

import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.{Counters, Ranking, View}

object ProcessWaiterActor {
  def apply(startTime: Long, renderActor: ActorRef[Ranking | Counters | String], rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Behavior[String] = Behaviors.receive[String]{ (_, _) =>
    renderActor ! " Stopped"
    Behaviors.stopped
  }.receiveSignal({ case (_, Terminated(_)) =>
    renderActor ! " Time to finish: " + (System.currentTimeMillis() - startTime) + "ms"
    rankingActor ! None
    countersActor ! None
    TerminationWaiterActor()
  })
}