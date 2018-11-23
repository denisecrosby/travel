package travelProjectWeb1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.out;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import static travelProjectWeb1.Login.*;

@Named(value = "register")
@ManagedBean
public class register { 
    private String id;
    private String password;
    private String tag;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public void register()
    {
        
    }
    
    public void register1() 
    {
        
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        //Access the database and insert a record into travelAccount
        //Make sure the accountID is unique
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from Account "
                    + "where username = '" + id + "'");
            if (rs.next()) {
                out.println("Account already exists.");
                register1();
                return;
            }
        //    stat.executeUpdate(String.format("Insert into account values 

            out.println("The new travel account is created!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
   
    
}
