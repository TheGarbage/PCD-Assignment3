package es1.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import java.io.File
import es1.resources.{Counters, Ranking, View}

object MainActor{

  object Command extends Enumeration {
    type Action = Value
    val Start, Stop, Shutdown = Value
  }

  final case class Msg(d: String, n: Int, maxl: Int, ni: Int, command: Command.Action)

  def apply(view: View, renderActor: ActorRef[(Option[Ranking],Option[Counters], Option[String])], startActor: Option[ActorRef[String]] = None): Behavior[Msg] = Behaviors.receive{ (ctx, msg) =>
    msg.command match {
      case Command.Start =>
        val directory: File = new File(msg.d)
        val directoryFiles: Array[File] = directory.listFiles()
        if (directoryFiles == null) {
          view.setFinish(" Invalid directory selected")
          Behaviors.same
        } else if (directoryFiles.isEmpty) {
          view.setFinish(" The selected directory is empty")
          Behaviors.same
        } else {
          val newStartActor = ctx.spawn(StartActor(renderActor, directory,msg.n, msg.maxl, msg.ni), System.currentTimeMillis() + "StartActor")
          MainActor(view, renderActor,Some(newStartActor))
        }
      case Command.Stop =>
        startActor match {
          case None =>
          case Some(newStartActor) => newStartActor ! "stop"
        }
        MainActor(view, renderActor)
      case Command.Shutdown =>
        Behaviors.stopped
    }
  }
}