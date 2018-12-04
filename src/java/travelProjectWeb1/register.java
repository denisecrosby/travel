package travelProjectWeb1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import static travelProjectWeb1.Login.*;

@Named(value = "register")
@RequestScoped
public class register {

    private String id;
    private String password;
    private int ques;
    private String answer;
    private  Part path;
    private FileInputStream imageInputStream = null;
    private String[] tags;
    private boolean success = false;
    private String errorMessage;

    public boolean success() {
        return success;
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
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            //return to internalError.xhtml
        }
        
        if (id.equals(password))
        {
            errorMessage = "ERROR: Password can't be the same as ID";
            return;
        }
        
        success = false;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        byte[] image=null;
        InputStream im = null;
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            PreparedStatement ps1 = conn.prepareStatement("select * from account where userName = ?");
            ps1.setString(1, id);
            if (ps1.executeQuery().next()) {
                errorMessage = "ERROR: User Name Already Exists";
            } else {
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
                PreparedStatement ps2 = conn.prepareStatement("insert into account values(?, ?, ?, ?, ?)");
                ps2.setString(1, id);
                ps2.setString(2, password);
                if (path != null) {
                    ps2.setBinaryStream(3, imageInputStream);
                } else {
                    ps2.setBinaryStream(3, im);
                }
                ps2.setInt(4, ques);
                ps2.setString(5, answer);
                for (String tag : tags) {
                    PreparedStatement ps3 = conn.prepareStatement("insert into acc_tag values(?, ?)");
                    ps3.setString(1, id);
                    ps3.setInt(2, Integer.parseInt(tag));
                    ps3.executeUpdate();
                }
                if (ps2.executeUpdate() > 0) {
                    success = true;
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
