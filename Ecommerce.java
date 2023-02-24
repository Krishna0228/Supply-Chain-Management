package com.example.ecommerce;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Ecommerce extends Application {

    private final int width=500,height=400,headerLine=50;
    ProductList productlist= new ProductList();
    Pane bodyPane;
    GridPane footerBar;
    Button signinButton=new Button("Sign In");
    Button placeOrderButton=new Button("Place Order");
    Label welcomelabel=new Label("Welcome!");
    Orders order=new Orders();
    ObservableList<Product> cartItemList= FXCollections.observableArrayList();
    Customer loggedinCustomer=null;
    private void addItemsToCart(Product product){
        if(cartItemList.contains(product))
            return;
        cartItemList.add(product);
        //System.out.println("Products in Cart "+cartItemList.stream().count());
    }
    private GridPane headerBar(){
        GridPane header=new GridPane();

        TextField searchbar=new TextField();
        Button searchbuttob=new Button("Search");
        Button cartButton=new Button("Cart");
        Button ordersButton=new Button("Orders");

        ordersButton.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(order.getOrderslist(cartItemList));
            }
        }));

        searchbuttob.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productlist.getAllProducts());
            }
        });

        signinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productlist.productsInCart(cartItemList));
            }
        });
        header.setHgap(10);
        header.add(searchbar,0,0);
        header.add(searchbuttob,1,0);
        header.add(signinButton,2,0);
        header.add(welcomelabel,3,0);
        header.add(cartButton,4,0);
        header.add(ordersButton,5,0);
        return header;
    }

    private GridPane loginPage(){
        Label userLabel=new Label("UserName");
        Label passLabel=new Label("Password");
        TextField userName=new TextField();
        userName.setPromptText("Enter User Name");
        PasswordField password=new PasswordField();
        password.setPromptText("Enter Password");
        Button loginButton=new Button("Login");
        Label messageLabel= new Label("Login-Message");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String user=userName.getText();
                String pass=password.getText();
                loggedinCustomer=login.customerlogin(user,pass);
                if(loggedinCustomer!=null){
                    messageLabel.setText("Login Successful!");
                    welcomelabel.setText("Welcome "+loggedinCustomer.getName());
                }
                else{
                    messageLabel.setText("Login Failed!");
                }
            }
        });
        GridPane loginPane=new GridPane();
        loginPane.setTranslateY(50);
        loginPane.setTranslateX(50);
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.add(userLabel,0,0);
        loginPane.add(userName,1,0);
        loginPane.add(passLabel,0,1);
        loginPane.add(password,1,1);
        loginPane.add(loginButton,0,2);
        loginPane.add(messageLabel,1,2);

        return loginPane;
    }
    private void showDialoge(String message){
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("Order Status");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText(message);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    private GridPane footerBar(){
        Button buyNowButton=new Button("Buy Now");
        Button addtocartButton=new Button("Add To Cart");

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productlist.getSelectedProduct();
                boolean orderStatus=false;
                if(product!=null && loggedinCustomer!=null){
                    orderStatus=order.placeOrder(loggedinCustomer,product);
                }
                if(orderStatus==true){
                    showDialoge("Order Successful");
                }else{
                    showDialoge("Order failed");
                }
            }
        });

        addtocartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productlist.getSelectedProduct();
                addItemsToCart(product);
            }
        });
        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int orderCount=0;
                if(!cartItemList.isEmpty() && loggedinCustomer!=null){
                    orderCount=order.placeOrderMultipleProducts(cartItemList,loggedinCustomer);
                }
                if(orderCount>0){
                    showDialoge("Order for "+orderCount +"  products placed Successfully");
                }else{
                    showDialoge("Order failed");
                }
            }
        });
        GridPane footer=new GridPane();
        footer.setHgap(10);
        footer.setTranslateY(headerLine+height);
        footer.setTranslateX(10);
        footer.add(buyNowButton,0,0);
        footer.add(addtocartButton,1,0);
        footer.add(placeOrderButton,2,0);

        return footer;
    }
    private Pane createContent(){
        Pane root=new Pane();

        root.setPrefSize(width,height+2*headerLine);

        bodyPane=new Pane();
        bodyPane.setPrefSize(width,height);
        bodyPane.setTranslateY(headerLine);
        bodyPane.setTranslateX(10);

        bodyPane.getChildren().add(loginPage());

        footerBar=footerBar();
        root.getChildren().addAll(headerBar(),
               // loginPage(),
               // productlist.getAllProducts(),
                bodyPane,
                footerBar

        );

        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {   //start method is the entry point for a JavaFX application
        //FXMLLoader fxmlLoader = new FXMLLoader(Ecommerce.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("Ecommerce");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();//calls start methhos and launches application
    }
}