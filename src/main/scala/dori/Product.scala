package dori
 import scala.collection.mutable.{Buffer, ListBuffer}
 import java.time.*


class Product(val name: String, val description: String, val producttype: String, val dailyPrice: Int, val weeklyPrice: Int, val monthlyPrice: Int, var availableCount: Int):

  override def toString: String = s"$name"

  var comments = ListBuffer[String]()

  var count = availableCount

  def info = s"$name, $description.\nPrices:\nDaily price: $dailyPrice, Weekly price: $weeklyPrice, Monthly price: $monthlyPrice\nComments:\n$comments."


  def addComment(c: String) =
   comments += c

  def getPrice(duration: Duration) =
    //Duration is in the from of days when we put .toDays(). So we need to count the months, weeks and days separately and divide and find remainders.
   val days = duration.toDays
   (days/30 * monthlyPrice) + ((days%30)/7 * weeklyPrice) + (((days%30)%7) * dailyPrice)
  end getPrice

end Product






