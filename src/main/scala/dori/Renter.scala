package dori
 import scala.collection.mutable.*
 import java.time.*
 import java.io.*

class Renter(val name: String, val contactInfo: String):


  var rentedProducts = ListBuffer[Product]()
  var comments = ListBuffer[String]()

  def info: String =
    s"$name\n${name.toLowerCase}@gmail.com\nComments:\n$comments, \nRented products:\n$rentedProducts."
  
  def addProduct(product: Product) =
    rentedProducts += product
    
  def removeProduct(product: Product) =
    rentedProducts -= product

  def addComment(comment: String) =
    comments += comment


end Renter

    