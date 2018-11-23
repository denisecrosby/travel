/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;

@Named(value = "register")
@ManagedBean
@SessionScoped
public class register {

     String id;
     String password;
     byte[] photo;
     int ques;
     String answer;
     public Part path;
     FileInputStream imageInputStream = null;
     String[] tags;
     String successMessage;
     String errorMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
     
     
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPath(Part path) {
        this.path = path;
    }

    public void setImageInputStream(FileInputStream imageInputStream) {
        this.imageInputStream = imageInputStream;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    

    public String getAnswer() {
        return answer;
    }

    public Part getPath() {
        return path;
    }

    public FileInputStream getImageInputStream() {
        return imageInputStream;
    }
    
    public int getQues() {
        return ques;
    }

    public void setQues(int ques) {
        this.ques = ques;
    }
    
    public register() 
    {
        
    } 
    public String insertregister()
    {
        String page="";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String URL= "jdbc:mysql://mis-sql.uhcl.edu/antaor0966";
        try {
            System.out.println("id:"+ id);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            conn = DriverManager.getConnection(URL, "antaor0966", "1637556");
            // fire select querry with the Id value given from front end if you get any rows then declare error message as 'User name already found'
             PreparedStatement ps3 = conn.prepareStatement(
                    "SELECT * from account WHERE userName  = ?");
             ps3.setString(1,id);
             ResultSet rs1 = ps3.executeQuery();
             if(rs1.next())
             {
                 errorMessage="User Name Already Exists";
             }
             else
             {
                System.out.println("connected ****************");
                System.out.println("path"+ path);
                InputStream input = path.getInputStream();
                System.out.println("getName: "+path.getName());
                PreparedStatement ps=conn.prepareStatement("insert into account values(?,?,?,?,?)");
            
            ps.setString(1, id);
            ps.setString(2, password);
            ps.setBinaryStream(3,input,3);
            ps.setInt(4, ques); 
            ps.setString(5, answer);
            int a1 = ps.executeUpdate();
           
            for (String tag : tags) {
                System.out.println("tag: "+ tag);     
                PreparedStatement ps1=conn.prepareStatement("insert into acc_tag values(?,?)");
                ps1.setString(1,id);
                ps1.setInt(2,Integer.parseInt(tag));
              ps1.executeUpdate();
            }

            System.out.println("successfully inserted");
            if(a1 >0)
            {
                successMessage="Account created Successfully";
            }
             }
            
            
            //final String db_url = "jdbc:mysql://mis-sql.uhcl.edu/antaor0966";
            //conn = DriverManager.getConnection(db_url, "antaor0966", "1637556");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        catch (IOException ex) {
             Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
         }finally {
            closeDatabase(rs, stat, conn);
        }
        System.out.println("before returning");
    return page;
    }
    
  
    
}
