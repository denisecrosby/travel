/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.getLogin;
import static travelProjectWeb1.Login.openDatabase;

/**
 *
 * @author raisa
 */
@Named(value = "review")
@ManagedBean
public class Review {
    String userName;
    String rev;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }
    
    public Review()
    {
        
    }
    
    public Review(String userName,String rev) 
    {
        this.userName=userName;
        this.rev=rev;
    }
   
    public void addReview(int att_id, String un) {
        int max = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            
            stat = conn.createStatement();
            rs=stat.executeQuery("select max(review_ID) from att_review");
            while(rs.next())
            {
                max=rs.getInt(1)+1;
            }
            stat.executeUpdate("insert into att_review values('" + max + "','" + att_id + "','" + un + "','" + rev + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
    
     
}
