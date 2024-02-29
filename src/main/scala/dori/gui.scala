package dori

import javafx.scene.control.DatePicker
import javafx.scene.layout.BorderPane
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color.*
import scalafx.scene.control.{Button, CheckBox, Label, TextField, TextFormatter}
import scalafx.scene.text.Font
import scalafx.scene.control.TextFormatter.Change
import scalafx.scene.layout.StackPane
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.layout.CornerRadii
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.RowConstraints
import scalafx.scene.paint.Color.*
import scalafx.Includes.*
import javafx.scene.control.{Menu, MenuBar, MenuButton, MenuItem}
import scalafx.event.ActionEvent
import scalafx.scene.control.Button.sfxButton2jfx

import java.time.*
import javafx.scene.control.DatePicker


object Main extends JFXApp3:

  def start(): Unit =


    stage = new JFXApp3.PrimaryStage:
      title = "Renting"
      width = 1600
      height = 900


    var root = GridPane()
    var root2 = GridPane()
    var root3 = GridPane()
    var root4 = GridPane()

    val scene1 = Scene(parent = root)
    stage.setScene(scene1)
    var scene2 = Scene(parent = root2)
    var scene3 = Scene(parent = root3)
    var scene4 = Scene(parent = root4)
    var rentingpage = new RentingPage("RentingPage")


    var btn14 = new Button:
      text = "Products"
      onAction = (event) => println(s"${rentingpage.products}")

    def ManageProduct() =
      val view = VBox()
      val textbox = new TextField
        root.add(view,0,1)
        view.children += Label(" To add product please input the name, description, the product type, daily price, weekly price, monthly price, the amount of product. \n Separating them with commas and not space bars.\n Example: Camera,Canon EOS 2000D,Electronics,50,300,1000,3 \n Then click 'Add Product'.\n To remove just provide the name of the product and click 'Remove Product'.")
        view.layoutX= 70
        view.layoutY = 40
        view.children+=textbox
      val add= new Button("Add Product")
      val remove= new Button("Remove Product")
      val back= new Button("Back")

        view.children += back
        view.children += add
        view.children += remove
        view.children += btn14
        back.onAction = (event)=>
          view.visible = false
          back.visible = false
        add.onAction = (event) => //Add the product described in textbox to stock.
         try
           val input = textbox.text.value.split(",")
           val name = input.head
           val desc = input(1)
           val ptype = input(2)
           val dprice = input(3).toInt
           val wprice = input(4).toInt
           val mprice = input(5).toInt
           val aC = input(6).toInt
           rentingpage.addProduct(Product(name, desc, ptype, dprice, wprice, mprice, aC))
           view.children += new Label("Product added to stock.")
         catch
           case a: IndexOutOfBoundsException => view.children += Label("Product couldn't be added. Please check for typos :)")
        remove.onAction = (event) => //Remove the product described in textbox from stock.
         try
           rentingpage.removeProduct(textbox.text.value)
           view.children += new Label("Product deleted from stock.")
         catch
           case b: IndexOutOfBoundsException => view.children += Label("Product couldn't be deleted. Please check for typos :)")
    end ManageProduct


    def renting() =
      val view = VBox()
      val textbox = new TextField
        root.add(view,0,1)
        view.children += Label(" To rent a product please provide the following input: the name of the product you'd like to rent, the amount, your name. \n Separating with commas and not space bars.\n Example: Camera,2,Aaron Tiller\n Provide the dates and times as well, then click 'Rent'.\n To return a product you wont need to input the dates and times. Then click 'Return'.")
        view.layoutX= 70
        view.layoutY = 40
        view.children += textbox

      view.children += Label(" Pick the starting date of rent")
      val pickstart = new DatePicker(LocalDate.now)
        view.children += pickstart

      view.children += Label(" Input the starting hour (HH) of rent")
      val hour = new TextField
        view.children += hour

      view.children += Label(" Pick the return date of rent")
      val pickend = new DatePicker(LocalDate.now)
        view.children += pickend

      view.children += Label(" Input the ending hour (HH) of rent")
      val hour2 = new TextField
        view.children += hour2

      val back = new Button("Back")
        view.children += back
        back.onAction = (event) =>
          view.visible = false

      val rent = new Button("Rent")
          view.children += rent
          rent.onAction = (event) =>
           try
              val input = textbox.text.value.split(",")
              val product = rentingpage.products.find(input.head==_.name).get
              val count = input(1).toInt
              val renter = new Renter(input(2), input(2))
              val start = pickstart.getValue.atTime(hour.text.value.toInt,00)
              val due = pickend.getValue.atTime(hour2.text.value.toInt,00)
              rentingpage.rentProduct(product, count, renter, start, due)
              view.children += new Label("Product successfully rented.")
           catch
              case c: IndexOutOfBoundsException => view.children += Label("Product couldn't be rented. Please check for typos :)")

      val retur = new Button("Return")
          view.children += retur
          retur.onAction = (actionEvent) => //return a product described in textbox2
            try
              val input = textbox.text.value.split(",")
              //From the rented products on this page, we need the exact one that we want to return so:
              val eproduct = rentingpage.rentals.filter(input.head == _.product.name)
              //From these products we need the exact one with the inputted amount:
              val eamount = eproduct.filter(input(1).toInt == _.productCount)
              //From these products we need the exact one with this specific renter. And if there are multiple, get the first one:
              val erenter = eamount.filter(input(2) == _.renter.name).head
              rentingpage.returnProduct(erenter)
              view.children += new Label("Product successfully returned.")
            catch
              case c: IndexOutOfBoundsException => view.children += Label("Product couldn't be returned. Please check for typos :). Or maybe it hasn't been rented at all.")

    end renting


    def current() =
      val view = VBox()
      root4.add(view,0,1)
      view.layoutX= 300
      view.layoutY = 600
      view.children += new Label(s" ${rentingpage.currentSituation}")
      val back = new Button("Back")
        view.children += back
        back.onAction = (event) =>
         view.visible = false
    end current



    def information() =
      val view = VBox()
      root4.add(view, 0, 1)
      view.layoutX = 70
      view.layoutY = 40
      val label3 = new Label(" To view info about a specific renter or product please provide which one it is, and their name.\n Separating with a comma and not space bars.\n Example: Renter,Aaron Tiller\n Then click 'Get Info'.")
      view.children += label3
      val textbox3 = new TextField()
      view.children += textbox3
      val back = new Button("Back")
        view.children += back
        back.onAction = (event) =>
         view.visible = false
      val get = new Button("Get Info")
        view.children += get
        get.onAction = (actionEvent) =>
            val input = textbox3.text.value.split(",")
            input.head match
             case a: "Renter" => view.children += new Label(s"${rentingpage.renters.find(input(1)==_.name).get.info}")
             case b: "Product" => view.children += new Label(s"${rentingpage.products.find(input(1)==_.name).get.info}")
             case _ => view.children += Label("\nInformation was not found. Please check for typos :)")
    end information



    def comments() =
      val view = VBox()
      root4.add(view, 0, 1)
      view.layoutX = 70
      view.layoutY = 40
      val label = new Label(" To add a comment about a specific renter or product please provide which one it is,\ntheir name, and your comment.\n Separating with commas and not space bars.\n Example: Product,Camera,Amazing quality!\n Then click 'Add Comment'.")
      view.children += label
      val textbox = new TextField()
      view.children += textbox
      val back = new Button("Back")
        view.children += back
        back.onAction = (event) =>
         view.visible = false
      val add = new Button("Add Comment")
       view.children += add
       add.onAction = (actionEvent) =>
         val input = textbox.text.value.split(",")
         input.head match
           case a: "Renter" =>
             rentingpage.renters.find(input(1)==_.name).get.addComment(input(2))
             view.children += Label(s"Comment successfully added to renter ${input(1)}.")
           case b: "Product" =>
             rentingpage.products.find(input(1)==_.name).get.addComment(input(2))
             view.children += Label(s"Comment successfully added to product ${input(1)}.")
           case _ => view.children += Label("\nComment couldn't be added. Please check for typos :)")
    end comments



    val btn = new Button:
      text = "Product Inventory"
      onAction = (event) => ManageProduct()

    val btn2 = new Button:
      text = "Rent and Return"
      onAction = (event) => renting()

    val btn3 = new Button:
      text = "Status and Feedback Center"
      onAction = (event) => stage.setScene(scene4)

    val btn6 = new Button:
      text = "Back"
      onAction = (event) => stage.setScene(scene1) //go back to main page

    val btn10 = new Button:
      text = "Back"
      onAction = (event) => stage.setScene(scene1) //go back to main page

    val btn11 = new Button:
      text = "View Current Situation"
      onAction = (event) => current()

    val btn12 = new Button:
      text = "Information"
      onAction = (event) => information()

    val btn13 = new Button:
      text = "Commenting"
      onAction = (event) => comments()


    val btn15 = new Button:
      text = "Save Rental Events"
      onAction = (event) =>
        rentingpage.record("rentalrecords.csv",rentingpage.rentals)

    val buttons = new HBox(100, btn, btn2, btn3)
     root.add(buttons, 10, 10)

    val buttons2 = new HBox(100, btn10, btn11, btn12, btn13, btn15)
     root4.add(buttons2, 10, 10)






