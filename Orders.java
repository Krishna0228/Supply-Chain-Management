package com.example.ecommerce;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class Orders {
     TableView<Product> orderTable;
    public  boolean placeOrder(Customer customer,Product product){
        try{
            String placeOrder="insert into orders(customer_id,product_id,status) values("+customer.getId()+","+product.getId()+",'Ordered')";
            DatabaseConnection dbconn=new DatabaseConnection();
            return dbconn.insertUpdate(placeOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public int placeOrderMultipleProducts(ObservableList<Product> productObservableList, Customer customer){
        int count=0;
        for(Product product:productObservableList){
            if(placeOrder(customer,product))
                count++;
        }
        return count;
    }
    /*public static Pane getOrders(){

        ObservableList<Product> productList=Orders.getOrders();

        return createTableFromList(productList);
    }*/

    public  Pane createTableFromList(ObservableList<Product> orderList){
        TableColumn id=new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name=new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price=new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

       /* ObservableList<Product> data= FXCollections.<Product>observableArrayList();
        data.addAll(new Product(123,"Laptop",(double)234.5),
                new Product(1234,"lappy",(double)234.5)
        );*/

        orderTable=new TableView<>();
        orderTable.setItems(orderList);
        orderTable.getColumns().addAll(id,name,price);

        Pane tablePane=new Pane();
        tablePane.getChildren().add(orderTable);
        return tablePane;
    }

    public Pane getOrder(Customer customer){
        String order="select orders.oid,products.name,products.price from orders inner join products on orders.product_id=products.pid where customer_id="+customer.getId();
        ObservableList<Product> orderList=Product.getProducts(order);
        return  createTableFromList(orderList);
    }
    public Pane getOrderslist(ObservableList<Product> orderList){
        return createTableFromList(orderList);
    }


}
