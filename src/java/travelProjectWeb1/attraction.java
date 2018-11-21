package travelProjectWeb1;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import static jdk.nashorn.internal.objects.NativeJava.type;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;

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
    public Part path=null;
    public byte[] image;
    public String[] tags;
    FileInputStream imageInputStream = null;

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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
        if (this.favorite.equals("true")) {
            return true;
        }
        return false;
    }

    public attraction()
    {
        
    }
    
    public attraction(String id, String name, String description,
            String city, String state, String favorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.favorite = favorite;

    }
    
    public attraction(String id, String name, String description,
            String city, String state,String un,byte[] image) 
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city= city;
        this.state = state;
        this.image=image;
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

    public void mark(String state) {
        String mark;
        if (state.equals("approve")) {
            mark = "2";
        } else {
            mark = "3";
        }

        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {

            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from attractions "
                    + "WHERE att_id = '" + this.id + "'");
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
     public void createAttraction(String userName) 
    {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        
        int max=0;
        int s=getStateID();
        int c=getCityID();
        try {
            
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            rs=stat.executeQuery("select max(att_ID) from attractions");
            if(rs.next())
            {
                max=rs.getByte(1)+1;
            }
            if(path==null)
            {
                imageInputStream = new FileInputStream(new File("C:\\Users\\raisa\\Downloads\\attractions-att_Img.jpg"));    
            }
            else
            {
            InputStream input = path.getInputStream();
            String fileName = path.getSubmittedFileName();
            System.out.println("filename>>>>>>>>>>>>>>>>>>>>>>>>>>"+fileName);
            imageInputStream = new FileInputStream(new File(fileName));
            }
            
            PreparedStatement ps=conn.prepareStatement("insert into attractions values(?,?,?,?,?,?,?,?)");
            ps.setInt(1, max);
            ps.setString(2, name);
            ps.setInt(3, s);
            ps.setInt(4, c);
            ps.setString(5, description);
            ps.setString(6, userName);
            ps.setInt(7, 1);
            ps.setBinaryStream(8,imageInputStream);
            int r = ps.executeUpdate();
            
            for(int i=0;i<tags.length;i++)
            {
                int a=stat.executeUpdate("insert into attraction_tag values('"+max+"','"+tags[i]+"')");
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
     public  int getStateID()
    {
        int s=0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        
        try {
            
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from att_state");
            while (rs.next()) 
            {
                if(rs.getString(2).equals(state))
                {
                    s=rs.getInt(1);
                }
            }
            
            //System.out.println("x: back to home page");
            //test = input.nextLine();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
                closeDatabase(rs, stat, conn);
        }
        return s;
    }
     
     
     //get city id
     public  int getCityID()
    {
        int c=0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        
        try {
            
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from att_city");
            while (rs.next()) 
            {
                if(rs.getString(3).equals(city))
                {
                    c=rs.getInt(1);
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
