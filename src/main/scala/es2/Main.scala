package es2

import akka.actor.AddressFromURIString
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.typed.{Cluster, Join}
import akka.remote.RemoteTransportException
import com.typesafe.config.ConfigFactory
import es1.resources.View

import scala.util.Random
object Main extends App {
  try {
    val system = WaiterActor.startup(1000)(WaiterActor())
    val seed = AddressFromURIString.parse("akka://ClusterSystem@127.0.0.1:1000")
    Cluster(system).manager ! Join(seed)
  } catch {
    case e: RemoteTransportException => {
      val random = new Random()
      val system = WaiterActor.startup(
      random.nextInt(65535)
      )(Prova())
        val seed = AddressFromURIString.parse("akka://ClusterSystem@127.0.0.1:1000")
      Cluster(system).manager ! Join(seed)
      }
  }

}