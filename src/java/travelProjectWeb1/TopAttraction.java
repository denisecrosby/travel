package travelProjectWeb1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import static travelProjectWeb1.Login.*;

/**
 *
 * @author Veeranki
 * updated by Denise Crosby 11/29/2018
 */
@ManagedBean
@SessionScoped
public class TopAttraction {
    
    private List<attraction> attraction;

    public List<attraction> getAttraction() {
        return attraction;
    }

    public void setAttraction(List<attraction> attraction) {
        this.attraction = attraction;
    }
    public TopAttraction()
    {
        System.out.println("In Constructor of TopAttractions");
        this.attraction=new ArrayList<>();
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        
        try {
                stat = conn.createStatement();
                rs=stat.executeQuery("select atc.city as city,att.att_name as attname,description , atss.state as state,ats.score as score from att_state atss " +
                                        " inner join att_city atc on atss.state_id=atc.state_id " +
                                        " inner join attractions att on atc.city_id=att.city_id " +
                                        " inner join att_score ats on att.att_id=ats.att_id where ats.score>4");
                
 		while (rs.next()) {
                    attraction att=new attraction();
                    att.setCity(rs.getString("city"));
                    att.setName(rs.getString("attname"));
                    att.setState(rs.getString("state"));
                    att.setAvg(rs.getFloat("score"));
                    att.setDescription(rs.getString("description"));
                    this.attraction.add(att);
                    
		}
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeDatabase(rs, stat, conn);
        }
    }
         
}
