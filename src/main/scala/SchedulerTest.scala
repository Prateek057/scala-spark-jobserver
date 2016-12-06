/**
  * Created by d799383 on 06.12.2016.
  */

import akka.actor.{Actor, ActorContext, ActorRef, Props}
import akka.event.Logging
import com.typesafe.config.Config
import org.apache.spark.SparkContext
import spark.jobserver.{SparkJob, SparkJobValidation}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class testActor extends Actor {
  val child = context.actorOf(Props.empty, "child")
  context.watch(child)
  // <-- this is the only call needed for registration
  var lastSender = context.system.deadLetters

  val log = Logging(context.system, this)

  def receive = {
    case "test" => log.info("received test")
    case Long => log.info("Time Received")
    case _ => log.info("received unknown message")
  }
}



object SchedulerTest extends SparkJob {

  def main(args: Array[String]): Unit = {
    val system = akka.actor.ActorSystem("system")

    val ta = system.actorOf(Props[testActor], name = "ta")


    system.scheduler.schedule(50 milliseconds, 1 hours) {
      ta ! System.currentTimeMillis
    }

    def runJob(sc: SparkContext, jobConfig: Config): Unit = {
      system.scheduler.schedule(50 milliseconds, 1 hours) {
        ta ! "test"
      }

    }

    def validate(sc: SparkContext, config: Config): SparkJobValidation = {

    }


  }


}
