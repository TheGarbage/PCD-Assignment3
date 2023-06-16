package es2.resources

import akka.actor.AddressFromURIString
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.cluster.typed.{Cluster, Join}
import akka.remote.RemoteTransportException
import akka.util.Helpers
import com.typesafe.config.ConfigFactory

import scala.util.Random
import es2.actors.Sender

import scala.annotation.tailrec


object Methods:
  val DEFAULT_PORT = 2551

  @tailrec
  def startup(port: Int = 2551): Unit = {

    try {
      val config = ConfigFactory
        .parseString(s"""akka.remote.artery.canonical.port=$port""")
        .withFallback(ConfigFactory.load("cluster"))
      val system = ActorSystem(Sender(), "ClusterSystem", config)
      val seed = AddressFromURIString.parse(s"akka://ClusterSystem@127.0.0.1:$DEFAULT_PORT")
      Cluster(system).manager ! Join(seed)
    } catch {
      case _: RemoteTransportException => startup(new Random().nextInt(65535))
    }
  }
