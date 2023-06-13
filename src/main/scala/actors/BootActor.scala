package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import java.io.File
import resources.{Counters, Ranking, View}

object BootActor{

  object Command extends Enumeration {
    type Action = Value
    val Start, Stop, Shutdown = Value
  }

  final case class Msg(d: String, n: Int, maxl: Int, ni: Int, command: Command.Action)

  def apply(view: View, startActor: Option[ActorRef[String]] = None): Behavior[Msg] = Behaviors.receive{ (ctx, msg) =>
    msg.command match {
      case Command.Start =>
        val directory: File = new File("C:\\Users\\gugli\\Downloads\\TestFolder")
        val directoryFiles: Array[File] = directory.listFiles()
        if (directoryFiles == null) {
          view.setFinish("Invalid directory selected")
        } else if (directoryFiles.isEmpty) {
          view.setFinish("The selected directory is empty")
        } else {
          val ranking = Ranking(msg.n)
          val newStartActor = ctx.spawn(StartActor(System.currentTimeMillis(), ranking, view, directory,
            ctx.spawn(RankingActor(ranking, view), "RankingActor"),
            ctx.spawn(CountersActor(Counters(msg.maxl, msg.ni), view), "CounterActor")), "StartActor")
          return BootActor(view, Some(newStartActor))
        }
        Behaviors.same
      case Command.Stop =>
        startActor match {
          case None =>
          //case Some(newStartActor) => newStartActor ! "stopped"
        }
        BootActor(view)
      case Command.Shutdown =>
        Behaviors.stopped
    }
  }
}