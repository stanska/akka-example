import java.text.SimpleDateFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._
import scala.util.Random

object kitchen extends App {

  /////////////////////////////
  // Some type aliases, just for getting more meaningful method signatures:
  type CoffeeBeans = String
  type GroundCoffee = String
  case class Water(temperature: Int)
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  // some exceptions for things that might go wrong in the individual steps
  // (we'll need some of them later, use the others when experimenting
  // with the code):
  case class GrindingException(msg: String) extends Exception(msg)
  case class FrothingException(msg: String) extends Exception(msg)
  case class WaterBoilingException(msg: String) extends Exception(msg)
  case class BrewingException(msg: String) extends Exception(msg)
  /////////////////////////////
  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = { "cappuccino = " + espresso + " + " + frothedMilk}

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    val beansTime = Random.nextInt(2000)
    Thread.sleep(beansTime)
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("finished grinding...Took " + beansTime)
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    val waterTime = Random.nextInt(2000)
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot! Took " + waterTime)
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system engaged!")
    val milkTime = Random.nextInt(2000)
    Thread.sleep(milkTime)
    println("shutting down milk frothing system. Took " + milkTime)
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    val coffeeTime = Random.nextInt(2000)
    Thread.sleep(coffeeTime)
    println("it's brewed! Took " + coffeeTime)
    "espresso"
  }
  ///////////// Business logic
  val startTime = System.currentTimeMillis()
  println("Kitched starting")
  def prepareCappuccino(): Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatedWater = heatWater(Water(20))
    val frothedMilk = frothMilk("milk")
    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }
  val capo = prepareCappuccino()
  val res = Await.ready(capo, 1 minutes)
  res.map(string => {
    println(string)
    val endTime = System.currentTimeMillis()
    println("Kitchen ending: Coffee took " + (endTime - startTime))
  });
}