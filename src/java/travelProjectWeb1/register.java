/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
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

    public register() {
    }

    public void insertregister() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = openDatabase();
            PreparedStatement ps1 = conn.prepareStatement("select * from account where userName = ?");
            ps1.setString(1, id);
            if (ps1.executeQuery().next()) {
                errorMessage = "User Name Already Exists";
            } else {
                InputStream input = path.getInputStream();
                PreparedStatement ps2 = conn.prepareStatement("insert into account values(?, ?, ?, ?, ?)");
                ps2.setString(1, id);
                ps2.setString(2, password);
                ps2.setBinaryStream(3, input, 3);
                ps2.setInt(4, ques);
                ps2.setString(5, answer);
                for (String tag : tags) {
                    PreparedStatement ps3 = conn.prepareStatement("insert into acc_tag values(?, ?)");
                    ps3.setString(1, id);
                    ps3.setInt(2, Integer.parseInt(tag));
                    ps3.executeUpdate();
                }
                if (ps2.executeUpdate() > 0) {
                    successMessage = "Account created Successfully";
                }
            }
        } catch (SQLException e) {
            errorMessage = "Internal error, please try again";
        } catch (IOException e) {
            errorMessage = "Missing image";
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
}
