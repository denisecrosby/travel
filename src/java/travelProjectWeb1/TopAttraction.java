/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import static travelProjectWeb1.Login.URL;

/**
 *
 * @author Veeranki
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
                rs=stat.executeQuery("select atc.city as city,att.att_name as attname,atss.state as state,ats.score as score from att_state atss " +
                                        " inner join att_city atc on atss.state_id=atc.state_id " +
                                        " inner join attractions att on atc.city_id=att.city_id " +
                                        " inner join att_score ats on att.att_id=ats.att_id where ats.score>4");
                
 		while (rs.next()) {
                    attraction att=new attraction();
                    att.setCity(rs.getString("city"));
                    att.setName(rs.getString("attname"));
                    att.setState(rs.getString("state"));
                    att.setAvg(rs.getFloat("score"));
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
         public static Connection openDatabase() {
            try {
                //connect to the db - login id and password below
                return DriverManager.getConnection(URL, "antaor0966", "1637556");
            } catch (SQLException e) {

                e.printStackTrace();
            }
            return null;
        }

        public static void closeDatabase(ResultSet rs, Statement stat, Connection conn) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
}
