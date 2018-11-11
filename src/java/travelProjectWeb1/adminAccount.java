package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;


public class adminAccount extends userAccount1
{

    public adminAccount(String accountId, String password) {
        super(accountId, password);
    }
    
        public ArrayList<String> viewMarked(String mark) {
      
        Statement stat = null;
        ResultSet rs = null;
        
        ArrayList<String> result = new ArrayList<>();

        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from attractions a, status s "
                    + "where s.statusNum = a.status and s.status = '" + mark + "'");
            while (rs.next()) {
                result.add(rs.getString("att_name"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;
        
    }
    
}