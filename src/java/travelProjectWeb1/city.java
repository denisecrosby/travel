package travelProjectWeb1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import static travelProjectWeb1.Login.*;

/* 
    Author    : raisa
*/

@ManagedBean
public class city 
{
    private List<String> city;
    
    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }
    public city()
    {
         city = new ArrayList<String> ();
       
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_city");
            while (rs.next()) 
            {
                city.add(rs.getString(3));
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs,stat,conn);
        }
    }
    
}
