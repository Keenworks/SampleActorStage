package com.keenworks.sample.sampleactorstage

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Stash}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.slf4j.LoggerFactory

object SampleActorStage extends App {
  private val log = LoggerFactory.getLogger(getClass)
  implicit val system = ActorSystem("SampleActorStage")
  implicit val materializer = ActorMaterializer()

  case class AssignStageActor(actorRef: ActorRef)

  val sourceFeeder: ActorRef = system.actorOf(Props(new Actor with Stash {
    def receive: Receive = {
      case _: String => stash()
      case AssignStageActor(stageActor: ActorRef) =>
        unstashAll()
        context.become(receiveNew(stageActor))
    }

    def receiveNew(stageActor: ActorRef): Receive = {
      case msg: String =>
        log.info("sourceFeeder received message, forwarding to stage: {} ", msg)
        stageActor ! msg
    }
  }))

  val sourceGraph: MessageSource = new MessageSource(sourceFeeder)
  val source: Source[String, _] = Source.fromGraph(sourceGraph)

  source.runForeach(msg => {
    log.info("Stream received message: {} ", msg)
  })

  sourceFeeder ! "One"
  sourceFeeder ! "Two"
  sourceFeeder ! "Three"

}
