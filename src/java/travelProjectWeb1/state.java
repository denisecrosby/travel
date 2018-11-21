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
        
       
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            final String db_url = "jdbc:mysql://mis-sql.uhcl.edu/antaor0966";
            conn = DriverManager.getConnection(db_url, "antaor0966", "1637556");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_state");
            while (rs.next()) 
            {
                state.add(rs.getString(2));
            }
            //System.out.println("x: back to home page");
            //test = input.nextLine();
        } catch (SQLException e) 
        {
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
