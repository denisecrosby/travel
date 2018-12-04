package travelProjectWeb1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.model.StreamedContent;
import static travelProjectWeb1.Login.*;

/* 
    Authors    : raisa, denise
 */
@Named(value = "attraction")
@ManagedBean
public class attraction {

    //attributes
    public String id;
    public String name;
    public String description;
    public String city;
    public String state;
    public String favorite;
    public float avg;
    public Part path = null;
    public byte[] image;
    public String[] tags;
    FileInputStream imageInputStream = null;
    InputStream im = null;
    public String new_state;
    public String new_city;
    public StreamedContent productImage;

    public StreamedContent getProductImage() {
        return productImage;
    }

    public void setProductImage(StreamedContent productImage) {
        this.productImage = productImage;
    }

    public String getNew_state() {
        return new_state;
    }

    public void setNew_state(String new_state) {
        this.new_state = new_state;
    }

    public String getNew_city() {
        return new_city;
    }

    public void setNew_city(String new_city) {
        this.new_city = new_city;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getFavorite() {
        return favorite;
    }

    public Part getPath() {
        return path;
    }

    public void setPath(Part path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isFavorite() {
        return this.favorite.equals("true");
    }

    public String favImg() {
        return isFavorite() ? "/image/heart-full.png" : "/image/heart-empty.png";
    }

    public String getStar() {
        return ("/image/stars/stars-" + Math.round(avg * 2) / 2.0 + ".png").replaceFirst("\\.", "_");
    }

    //constructors
    public attraction() {
    }

    public attraction(StreamedContent image) {
        this.productImage = image;
        System.out.println("..............single..." + image);
    }

    public attraction(String id, String name, String description,
            String city, String state, String favorite, float avg) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.favorite = favorite;
        this.avg = avg;
    }

    public attraction(String id, String name, String description,
            String city, String state, String favorite, float avg, StreamedContent image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.favorite = favorite;
        this.avg = avg;
        this.productImage = image;
        System.out.println("................all." + image);
    }

    public attraction(String id, String name, String description,
            String city, String state, String un, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.image = image;
    }

    //this constructor is used by the admin to view, approve, and reject attractions (they don't need favorites)
    public attraction(String id, String name, String description,
            String city, String state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
    }

    //this method is used by the admin to approve to reject attractions
    public void mark(String state) {
        String mark = state.equals("approve") ? "2" : "3";

        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {

            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from attractions WHERE att_id = '" + id + "'");
            while (rs.next()) {
                rs.updateString("status", mark);
                rs.updateRow();
            }

            rs = stat.executeQuery("select * from att_state WHERE state_ID = (select state_id from attractions where att_id = '" + id + "')");
            while (rs.next()) {
                rs.updateInt("add_delete", 1);
                rs.updateRow();
            }

            rs = stat.executeQuery("select * from att_city WHERE city_ID = (select city_id from attractions where att_id = '" + id + "')");
            while (rs.next()) {
                rs.updateInt("add_delete", 1);
                rs.updateRow();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }

    public String title() {
        return String.format("%s, %s %s", name, city, state);
    }

    public void addFavorite(String userID) {
        Connection conn = openDatabase();
        Statement stat = null;
        try {

            stat = conn.createStatement();
            stat.executeUpdate("insert into myfavoritedes values ('" + this.getId() + "', '" + userID + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(null, stat, conn);
        }
    }

    //the favorite method returns true if this attraction is already marked as a favorite for the logged in user
    public boolean favorite(String userID) {

        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {

            stat = conn.createStatement();
            rs = stat.executeQuery("select * from myfavoritedes "
                    + "WHERE att_id = '" + this.id + "' and username = '" + userID + "'");
            if (rs.next()) {

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return false;
    }

    //method to create attraction
    public String createAttraction(String userName) {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        boolean s_c = false;
        boolean ct = false;
        boolean exist = false;
        if (state.equals("Other")) {
            s_c = true;
        } else if (city.equals("Other")) {
            ct = true;
        }
        int max = 0;
        int s = getStateID();
        int c = getCityID();
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from attractions where att_Name='" + name + "'");
            if (rs.next()) {
                exist = true;
            }
            if (exist == true) {
                FacesMessage message = new FacesMessage("Attracttion name already exist");
                FacesContext.getCurrentInstance().addMessage("a_form:att_name", message);
                return "creacteAttraction.xhtml";
            } else {
                rs = stat.executeQuery("select max(att_ID) from attractions");
                if (rs.next()) {
                    max = rs.getByte(1) + 1;
                }

                if (path == null) {
                    rs = stat.executeQuery("select * from default_img where name='attraction'");
                    while (rs.next()) {
                        image = rs.getBytes(2);
                        im = new ByteArrayInputStream(image);
                    }

                } else {
                    InputStream input = path.getInputStream();
                    String fileName = path.getSubmittedFileName();
                    imageInputStream = new FileInputStream(new File(fileName));
                }

                PreparedStatement ps = conn.prepareStatement("insert into attractions values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, max);
                ps.setString(2, name);
                ps.setInt(3, s);
                ps.setInt(4, c);
                ps.setString(5, description);
                ps.setString(6, userName);
                ps.setInt(7, 1);
                if (path != null) {
                    ps.setBinaryStream(8, imageInputStream);
                } else {
                    ps.setBinaryStream(8, im);
                }
                ps.setInt(9, 0);
                int r = ps.executeUpdate();

                for (String tag : tags) {
                    stat.executeUpdate("insert into attraction_tag values('" + max + "','" + tag + "')");
                }
                if (s_c == true) {
                    return "add_state.xhtml";
                } else if (ct == true) {
                    return "add_city.xhtml";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(attraction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "att_successful";
    }

    public String view_Att() {
        getLogin().att_id = Integer.parseInt(id);
        getLogin().currentAtt = this;
        int var = getLogin().att_id;
        userAccount1 u = new userAccount1(var);
        return "attraction.xhtml";
    }

    //get state id
    public int getStateID() {
        int s = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_state");
            while (rs.next()) {
                if (rs.getString(2).equals(state)) {
                    s = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return s;
    }

    //get city id
    public int getCityID() {
        int c = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_city");
            while (rs.next()) {
                if (rs.getString(3).equals(city)) {
                    c = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return c;
    }

    public String add_state() {
        int max_s = 0;
        int max_c = 0;
        int max_a = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(state_ID) from att_state");
            while (rs.next()) {
                max_s = rs.getInt(1) + 1;
            }
            stat.executeUpdate("insert into att_state values('" + max_s + "','" + new_state + "',false)");
            stat.close();
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(city_ID) from att_city");
            while (rs.next()) {
                max_c = rs.getInt(1) + 1;
            }
            stat.executeUpdate("insert into att_city values('" + max_c + "','" + max_s + "','" + new_city + "',false)");
            stat.close();
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(att_ID) from attractions");
            while (rs.next()) {
                max_a = rs.getInt(1);
            }
            stat.close();
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE `attractions` SET `state_ID`='" + max_s + "',`city_ID`='" + max_c + "' WHERE att_ID='" + max_a + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "att_successful.xhtml";
    }

    public String add_city() {
        int s = 0;
        int max_c = 0;
        int max_a = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(att_ID) from attractions");
            while (rs.next()) {
                max_a = rs.getInt(1);
            }
            stat.close();
            stat = conn.createStatement();
            rs = stat.executeQuery("select state_ID from attractions where att_ID='" + max_a + "'");
            while (rs.next()) {
                s = rs.getInt(1);
            }
            stat.close();
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(city_ID) from att_city");
            while (rs.next()) {
                max_c = rs.getInt(1) + 1;
            }
            stat.executeUpdate("insert into att_city values('" + max_c + "','" + s + "','" + new_city + "',false)");
            stat.close();
            stat = conn.createStatement();

            stat.executeUpdate("UPDATE `attractions` SET `city_ID`='" + max_c + "' WHERE att_ID='" + max_a + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "att_successful.xhtml";
    }
}
