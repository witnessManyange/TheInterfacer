/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class DatabaseConnection {
    
    
     
    public static String host ="jdbc:mysql://localhost:3306/tracker";
    public static String username ="root";
    public static String password ="";   //  
    
      
    public Connection connect() throws SQLException
    {

            Connection con = (Connection) DriverManager.getConnection(host,username,password);
             return con; 
     }//end connect
  
}

