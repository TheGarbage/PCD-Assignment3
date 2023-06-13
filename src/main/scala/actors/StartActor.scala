package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, Ranking, View}

import java.io.File

object StartActor {
  def apply(view: View, directory: File, n: Int, maxl: Int, ni: Int): Behavior[String] = Behaviors.setup { ctx =>
    val child = ctx.spawn(DirectoryActor(directory,
      ctx.spawn(RankingActor(Ranking(n), view), System.currentTimeMillis() + "RankingActor"),
      ctx.spawn(CountersActor(Counters(maxl, ni), view), System.currentTimeMillis() + "CounterActor")
    ), System.currentTimeMillis() + "Directory")
    ctx.watch(child)
    view.setExecution(" Process...")
    ProcessWaiter(System.currentTimeMillis(), view)
  }
}