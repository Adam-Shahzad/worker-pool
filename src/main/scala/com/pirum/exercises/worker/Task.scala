package com.pirum.exercises.worker
import scala.concurrent.duration.Duration

trait Task
case class SuccessfulTask(seconds: Duration) extends Task
case class FailedTask(seconds: Duration) extends Task
case class TimeoutTask() extends Task


case class Result(successful: Seq[SuccessfulTask], failed: Seq[FailedTask], timedOut: Seq[TimeoutTask])
