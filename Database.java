/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receipts;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database{

    private static PreparedStatement pstmt;
    private static ResultSet rs;
    private static Connection con = null;
    private static DatabaseConnection db = new DatabaseConnection();
    
    
    
    public static ResultSet getSelectResult(String query,Connection con) throws SQLException{
        if(con == null)
            con = db.connect();
        
        pstmt = con.prepareStatement(query);
        rs = pstmt.executeQuery();
        return rs;
    }
    
    public static void insertRecord(String query,Connection con) throws SQLException{
        if(con == null)
            con = db.connect();
        
        pstmt = con.prepareStatement(query);
        pstmt.execute();
    }
    
    public static void updateRecord(String query, String receiptNumber, String accountNumber, String id,Connection con) throws SQLException{
        if(con == null)
            con = db.connect();
        
        pstmt = con.prepareStatement(query);
        pstmt.setString(1,receiptNumber);
        pstmt.setString(2, accountNumber);
        pstmt.setString(3, id);
        pstmt.executeUpdate();
    }
}
