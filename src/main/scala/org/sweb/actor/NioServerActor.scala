package org.sweb.actor

import java.nio.ByteBuffer

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import org.sweb.protocol.Response


class NIOServerActor(nworkers: Int) extends Actor {

  val manager = context.actorOf(Manager.props(nworkers))

  manager ! Initial()

  override def receive: Receive = {

    case ProcessRequest(read, transform, process, write) => {
      println("NIOServerActor: ProcessRequest")
      val buffer = read()
      println("NIOServerActor: read " + buffer.limit())
      manager ! RequestMessage(transform, process, write, buffer)
    }

    case RequestFinished(response, write) => {
      write(response)
    }

  }
}


object NIOServerActor {
  def props(nworkers: Int): Props = Props(new NIOServerActor(nworkers))
}
