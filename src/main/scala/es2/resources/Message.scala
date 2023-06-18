package es2.resources

import akka.actor.typed.ActorRef
import es2.example.BrushManager.Brush
import es2.example.PixelGrid

object Message:
  object Command extends Enumeration {
    type Action = Value
    val init, sendInit, brushColorChange, gridColorChange, mousePositionChange, removeBrush = Value
  }

  final case class Msg(command: Command.Action,
                       x: Option[Int],
                       y: Option[Int],
                       id: Option[Int],
                       color: Option[Int],
                       grid: Option[PixelGrid],
                       receiver: Option[ActorRef[Msg]]
                      )