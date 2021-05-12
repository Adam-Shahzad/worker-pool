package com.pirum.exercises.worker
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpec

object MainSpec extends AnyWordSpec with Matchers with Eventually {
  "Main.program" should {
    "Handle Failed and successful tasks" in {
      eventually {

      Main
        .program(
          Seq(
            FailedTask(3.seconds),
            SuccessfulTask(4.seconds),
            SuccessfulTask(2.seconds),
            FailedTask(1.seconds)
          ),
          timeout = 8.seconds,
          workers = 4
        )
        .futureValue match {
        case res => res shouldBe Result(
          Seq(SuccessfulTask(4.seconds), SuccessfulTask(2.seconds)),
          Seq(FailedTask(3.seconds), FailedTask(1.seconds)),
          Seq.empty)
        case _ => fail(s"something went wrong")
      }
    }
    }

    "Handle Failed and successful and timeout tasks" in {
      eventually {

        Main
          .program(
            Seq(
              FailedTask(3.seconds),
              TimeoutTask(),
              SuccessfulTask(4.seconds),
              SuccessfulTask(2.seconds),
              FailedTask(1.seconds)
            ),
            timeout = 8.seconds,
            workers = 4
          )
          .futureValue match {
          case res => res shouldBe Result(
            Seq(SuccessfulTask(4.seconds), SuccessfulTask(2.seconds)),
            Seq(FailedTask(3.seconds), FailedTask(1.seconds)),
            Seq(TimeoutTask()))
          case _ => fail(s"something went wrong")
        }
      }
    }
  }
}
