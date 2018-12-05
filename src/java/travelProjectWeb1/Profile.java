/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.getLogin;
import static travelProjectWeb1.Login.openDatabase;

/**
 *
 * @author raisa
 */
@Named(value = "profile")
@ManagedBean
public class Profile {
public Part path = null;
FileInputStream imageInputStream = null;
String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Part getPath() {
        return path;
    }

    public void setPath(Part path) {
        this.path = path;
    }

    public FileInputStream getImageInputStream() {
        return imageInputStream;
    }

    public void setImageInputStream(FileInputStream imageInputStream) {
        this.imageInputStream = imageInputStream;
    }
    
    
    public Profile() {
    }
    
    public ArrayList<userAccount1> viewProfile()
    {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<userAccount1> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from account where userName='" + getLogin().accountID+ "' and password='"+getLogin().password+"'");
            while (rs.next()) {
                byte[] localimage = rs.getBytes(3);
                StreamedContent sc = new DefaultStreamedContent(new ByteArrayInputStream(localimage));
                result.add(new userAccount1(rs.getString(1), rs.getString(2),sc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;
    }
    
    public String addProfileImage(String un) 
    {
        Statement stat = null;
        ResultSet rs = null;
        byte[] image;
        InputStream im = null;
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            if (path == null) {
                    rs = stat.executeQuery("select * from default_img where name='profile_img'");
                    while (rs.next()) {
                        image = rs.getBytes(2);
                        im = new ByteArrayInputStream(image);
                    }

                } else {
                    InputStream input = path.getInputStream();
                    String fileName = path.getSubmittedFileName();
                    imageInputStream = new FileInputStream(new File(fileName));
                    
                }
            PreparedStatement ps = conn.prepareStatement("update account set user_Img=? where userName='"+un+"'");
            if (path != null) {
                    ps.setBinaryStream(1, imageInputStream);
                } else {
                    ps.setBinaryStream(8, im);
                }
            int r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
        Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
            closeDatabase(rs, stat, conn);
        }
        return "account.xhtml";
    }
    
    public String resetPassword(String un){
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            
            stat = conn.createStatement();
            int r=stat.executeUpdate("update account set password='"+password+"' where userName='"+un+"'");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "index.xhtml";
    }
    
}
