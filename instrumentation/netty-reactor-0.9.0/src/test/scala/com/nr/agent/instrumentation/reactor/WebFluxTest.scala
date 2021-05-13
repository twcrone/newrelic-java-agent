package com.nr.agent.instrumentation.reactor

import com.newrelic.agent.introspec.{InstrumentationTestConfig, InstrumentationTestRunner, Introspector, TraceSegment, TransactionTrace}
import com.newrelic.api.agent.{Trace, TraceByReturnType, TraceLambda}
import org.junit.runner.RunWith
import org.junit.{Assert, Test}
import reactor.core.publisher.Mono

import java.util.concurrent.TimeUnit
import scala.jdk.CollectionConverters._

@RunWith(classOf[InstrumentationTestRunner])
@InstrumentationTestConfig(includePrefixes = Array("reactor"))
class WebFluxTest {

  @Test
  def singleMono(): Unit = {
    val result = Mono
      .just(1)
      .block()
    println(result)
  }

  @Test
  def oneTransaction(): Unit = {
    //Given
    implicit val introspector: Introspector = InstrumentationTestRunner.getIntrospector

    //When
    val result = getGetThreeResults.block()

    //Then
    introspector.getFinishedTransactionCount(TimeUnit.SECONDS.toMillis(10))

    val traces = getTraces()
    val segments = getSegments(traces)

    Assert.assertEquals("Result", 6, result)
    Assert.assertEquals("Transactions", 1, introspector.getTransactionNames.size)
    Assert.assertEquals("Traces", 1, traces.size)
    Assert.assertEquals("Segments", 2, segments.size)
  }

  @Trace(dispatcher = true)
  def getGetThreeResults: Mono[Integer] = {
    println("SSSSS getGetThreeResults")
    getFirstNumber
      .flatMap((x: Int) => getSecondNumber
        .flatMap((y: Int) => getThirdNumber
          .map((z: Int) => x + y + z)))
  }

  @Trace
  def getFirstNumber: Mono[Int] = {
    println("SSSSS getFirstNumber")
    val result = Mono.just({
      println("RRRRR getFirstNumber")
      1
    })
    println("FFFFF getFirstNumber")
    result
  }

  @Trace
  def getSecondNumber: Mono[Int] = {
    println("SSSSS getSecondNumber")
    val result = Mono.just({
      println("RRRRR getSecondNumber")
      2
    })
    println("FFFFF getSecondNumber")
    result
  }

  @Trace
  def getThirdNumber: Mono[Int] = {
    println("SSSSS getThirdNumber")
    val result = Mono.just({
      println("RRRRR getThirdNumber")
      3
    })
    println("FFFFF getThirdNumber")
    result
  }

  def getTraces()(implicit introspector: Introspector): Iterable[TransactionTrace] =
    introspector.getTransactionNames.asScala.flatMap(transactionName => introspector.getTransactionTracesForTransaction(transactionName).asScala)

  def getSegments(traces : Iterable[TransactionTrace]): Iterable[TraceSegment] =
    traces.flatMap(trace => this.getSegments(trace.getInitialTraceSegment))

  private def getSegments(segment: TraceSegment): List[TraceSegment] = {
    val childSegments = segment.getChildren.asScala.flatMap(childSegment => getSegments(childSegment)).toList
    segment :: childSegments
  }
}
