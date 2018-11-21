package travelProjectWeb1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;

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
       
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            final String db_url = "jdbc:mysql://mis-sql.uhcl.edu/antaor0966";
            conn = DriverManager.getConnection(db_url, "antaor0966", "1637556");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_city");
            while (rs.next()) 
            {
                city.add(rs.getString(3));
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
