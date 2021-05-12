package com.pirum.exercises.worker

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait Program {
  def program(tasks: Seq[Task], timeout: FiniteDuration, workers: Int): Future[Result]
}
