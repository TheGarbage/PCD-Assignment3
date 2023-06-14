package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, Ranking, View}

import java.io.File

object StartActor {
  def apply(renderActor: ActorRef[(Option[Ranking],Option[Counters], Option[String])], directory: File, n: Int, maxl: Int, ni: Int): Behavior[String] = Behaviors.setup { ctx =>
    val rankingActor = ctx.spawn(RankingActor(Ranking(n), renderActor), System.currentTimeMillis() + "RankingActor")
    ctx.watch(rankingActor)
    val countersActor = ctx.spawn(CountersActor(Counters(maxl, ni), renderActor), System.currentTimeMillis() + "CounterActor")
    ctx.watch(countersActor)
    val directoryActor = ctx.spawn(DirectoryActor(directory, rankingActor, countersActor), System.currentTimeMillis() + "Directory")
    ctx.watch(directoryActor)
    ProcessWaiterActor(System.currentTimeMillis(), renderActor, rankingActor, countersActor)
  }
}