package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, Ranking, View}

import java.io.File

object StartActor {
  def apply(view: View, directory: File, n: Int, maxl: Int, ni: Int): Behavior[String] = Behaviors.setup { ctx =>
    val rankingActor = ctx.spawn(RankingActor(Ranking(n), view), System.currentTimeMillis() + "RankingActor")
    ctx.watch(rankingActor)
    val countersActor = ctx.spawn(CountersActor(Counters(maxl, ni), view), System.currentTimeMillis() + "CounterActor")
    ctx.watch(countersActor)
    val directoryActor = ctx.spawn(DirectoryActor(directory, rankingActor, countersActor), System.currentTimeMillis() + "Directory")
    ctx.watch(directoryActor)
    view.setExecution(" Process...")
    ProcessWaiterActor(System.currentTimeMillis(), view, rankingActor, countersActor)
  }
}