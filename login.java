package com.example.ecommerce;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

public class login {

    private static byte[] getSha(String input){
        try{
            MessageDigest md= MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getEncryptedPassword(String password){
        try{
            BigInteger num=new BigInteger(1,getSha(password));
            StringBuilder hexString=new StringBuilder(num.toString(16));
            return hexString.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Customer customerlogin(String useremail,String password){
        String encryptedPass=getEncryptedPassword(password);
        String query="select * from customers where email='"+useremail+"' and password='"+encryptedPass+"'";
        DatabaseConnection dbconn=new DatabaseConnection();
        try {
            ResultSet rs = dbconn.getQueryTable(query);
            if (rs != null && rs.next()) {
                return new Customer(
                        rs.getInt("cid"),
                        rs.getString("name"),
                        rs.getString("email")
                        );
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

   /* public static void main(String[] args) {
        System.out.println(customerlogin("rihana@gmail.com","abc"));
        System.out.println(getEncryptedPassword("123"));
    }*/
}
