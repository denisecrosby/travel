package travelProjectWeb1;

import java.sql.*;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import static travelProjectWeb1.Login.*;

@Named(value = "register")
@ManagedBean
public class register {

    private String id;
    private String password;
    private String tag;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void register() {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from account where username = '" + id + "'");
            if (rs.next()) {
                register();
                return;
            }
            stat.executeUpdate("insert into account (username,password) values('" + id + "', '" + password + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
}
