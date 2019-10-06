package stdmgmtapp.db;


import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hp
 */
public class DbConnection implements DbProperties{
    
    public static Connection getConnection(){
    Connection con=null;
    try{
        Class.forName(DRIVER);
        con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
    catch(Exception ex){
        System.out.println(ex.getMessage());
    }
    return con;
}
}
