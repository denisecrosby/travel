package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.primefaces.model.StreamedContent;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;

/* 
    Author    : denise
 */
public class adminAccount extends userAccount1 {

    public adminAccount(String accountId, String password) {
        super(accountId, password);
        isAdmin = true;
    }

    public ArrayList<attraction> viewMarked(String mark) {

        Statement stat = null;
        ResultSet rs = null;

        ArrayList<attraction> result = new ArrayList<>();

        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select att_id,att_name,description,city,state "
                    + " , (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, att_state state, att_city c "
                    + " where a.state_id = state.state_id and a.city_id = c.city_id "
                    + " and s.statusNum = a.status and s.status = '" + mark + "'");
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("city"), rs.getString("state"), rs.getString("favorite"), rs.getFloat("avg")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }
}
