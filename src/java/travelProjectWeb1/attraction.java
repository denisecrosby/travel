package travelProjectWeb1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import static travelProjectWeb1.Login.*;

/* 
    Authors    : raisa, denise
 */
@Named(value = "attraction")
@ManagedBean
public class attraction {

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

    public attraction() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
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
    public void createAttraction(String userName) {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        int max = 0;
        int s = getStateID();
        int c = getCityID();
        try {
            imageInputStream = new FileInputStream(new File(path.getSubmittedFileName()));

            PreparedStatement ps = conn.prepareStatement("insert into attractions values(?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, max);
            ps.setString(2, name);
            ps.setInt(3, s);
            ps.setInt(4, c);
            ps.setString(5, description);
            ps.setString(6, userName);
            ps.setInt(7, 1);
            ps.setBinaryStream(8, imageInputStream);
            ps.executeUpdate();
            for (String tag : tags) {
                stat.executeUpdate("insert into attraction_tag values('" + max + "','" + tag + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(attraction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDatabase(rs, stat, conn);
        }
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
}
