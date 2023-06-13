package resources

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext

import java.io.File

object Common {
  val STRING_PREFIX = "0000000000"
  val MAX_DIGITS: Int = STRING_PREFIX.length
}