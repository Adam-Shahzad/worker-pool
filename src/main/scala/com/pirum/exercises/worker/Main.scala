package com.pirum.exercises.worker

import java.util.concurrent.Executors
import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.{Failure, Success, Try}
case class input(tasks: Seq[Task], timeout: FiniteDuration, workers: Int)

object Main extends App with Program {

  override def program(
      tasks: Seq[Task],
      timeout: FiniteDuration,
      workers: Int
  ): Future[Result] = {
    val result: Result =   Result(Seq.empty[SuccessfulTask], List.empty[FailedTask], List.empty[TimeoutTask])
    val workerThreads = Executors.newFixedThreadPool(workers)

    Try {
      tasks.map {
        case SuccessfulTask(seconds) =>
          result.successful.++(Seq(SuccessfulTask(seconds)))
          workerThreads.execute(resolveTaskSuccess(seconds))
        case FailedTask(seconds) =>
          result.successful.++(Seq(FailedTask(seconds)))
          workerThreads.execute(resolveTaskFailure(seconds))
        case TimeoutTask() =>
          result.successful.++(Seq(TimeoutTask()))
          workerThreads.execute(resolveTaskTimeout(timeout))
      }
    } match {
      case Success(_) => Future.successful(result)
      case Failure(t) => Future.failed(t)
    }
  }

  def resolveTaskSuccess(seconds: Duration): Runnable = () =>
    Thread.sleep(seconds.toSeconds * 1000)

  def resolveTaskFailure(seconds: Duration): Runnable = () =>
    Thread.sleep(seconds.toSeconds * 1000)

  def resolveTaskTimeout(timeout: FiniteDuration): Runnable = () =>
    Thread.sleep(timeout.toMillis)
}

