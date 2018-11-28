package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import static travelProjectWeb1.Login.*;

/* 
    Authors    : raisa
*/

@ManagedBean
@SessionScoped
public class state 
{
    private List<String> state;

    
    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }
    public state()
    {
         state = new ArrayList<String> ();
               
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_state where Add_Delete=true");
            while (rs.next()) 
            {
                state.add(rs.getString(2));
            }
            state.add("Other");
        } catch (SQLException e) 
        {
            e.printStackTrace();
        } finally {
           closeDatabase(rs,stat,conn);
        }
    }
    
   
}
