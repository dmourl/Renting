package dori
 import scala.collection.mutable.*
 import java.time.*
 import java.io.*


class RentingPage(val name: String):


 var products = ListBuffer[Product]()
 var renters = ListBuffer[Renter]()
 var rentedproducts = ListBuffer[Product]()
 var rentals = ListBuffer[Rental]()


 def addProduct(a: Product) =
   for i <- 0 until a.availableCount do
     products += a
   a.count += a.availableCount

 def removeProduct(b: String) =
   val theone = products.find(_.name == b).get
   products -= theone
   theone.count -= 1

 /**informs whether or not the product is available for renting***/
 def available(product: Product) =
   products.exists(n => n.name == product.name)

 /**if product is available, makes a reservation for a period of time with its cost as well. The reservation is recorded in the variable 'record' which will be its own file***/
 //We need to input all the parameters that are needed to make a Rental-object.
 //If the item is not available throw error.
 def rentProduct(product: Product, count1: Int, renter: Renter, startDate: LocalDateTime, dueDate: LocalDateTime) =
  val newRental = Rental(product, count1, renter, startDate, dueDate, product.getPrice(Duration.between(startDate, dueDate)))
  if available(product) && product.count>=count1 then
    product.count -= count1
    rentals += newRental
    renters += renter
    for i <- 0 until count1 do products -= product
    for i <- 0 until count1 do rentedproducts += product
    renter.rentedProducts += product
 //Try to return a confirmation message to the renter
  else
    throw IllegalArgumentException("No such product available.")


 def returnProduct(rental: Rental) =
  if rentedproducts.exists(n => n.name == rental.product.name) then
    rental.product.count += rental.productCount
    addProduct(Product(rental.product.name, rental.product.description, rental.product.producttype, rental.product.dailyPrice, rental.product.dailyPrice, rental.product.dailyPrice, rental.productCount)) // for i <- 0 until rental.productCount
    rentedproducts -= rental.product //for i <- 0 until rental.productCount
    rental.renter.removeProduct(rental.product)
  else
    throw IllegalArgumentException("This item has not been rented.")


 def record(name: String, rentals: ListBuffer[Rental]) =
   //save the rental transactions that have been made to a file
   val printer = new PrintWriter(new File(name)) //new file and printwriter
   rentals.foreach(rental => printer.println(s"${rental.productCount} ${rental.product.name}(s) was/were rented by ${rental.renter.name} from ${rental.startDate} to ${rental.dueDate} for the price of ${rental.cost} euros."))
   printer.close()


 def currentSituation = s"Currently there are ${products.size} products available for rent, ${rentedproducts.size} products are rented."

end RentingPage

