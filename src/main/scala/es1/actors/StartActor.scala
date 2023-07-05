package es1.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.{Counters, Ranking, View}

import java.io.File

object StartActor {
  def apply(renderActor: ActorRef[Ranking | Counters | String], directory: File, n: Int, maxl: Int, ni: Int): Behavior[String] = Behaviors.setup { ctx =>
    val rankingActor = ctx.spawnAnonymous(RankingActor(Ranking(n), renderActor))
    ctx.watch(rankingActor)
    val countersActor = ctx.spawnAnonymous(CountersActor(Counters(maxl, ni), renderActor))
    ctx.watch(countersActor)
    val directoryActor = ctx.spawnAnonymous(DirectoryActor(directory, rankingActor, countersActor))
    ctx.watch(directoryActor)
    ProcessWaiterActor(System.currentTimeMillis(), renderActor, rankingActor, countersActor)
  }
}