package org.sweb.actor

import akka.actor.{ActorRef, ActorSystem}


class SwebActorSystem {

  var serverActor: ActorRef = null

  def start(): Unit = {

    val system = ActorSystem("sweb-actors")
    println("SwebActorSystem: start")
    serverActor = system.actorOf(NIOServerActor.props(10))

  }

  def sendMessage(message: AnyRef): Unit = {
    println("SwebActorSystem: sendMessage")
    serverActor ! message
  }

}
