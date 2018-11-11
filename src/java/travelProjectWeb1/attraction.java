package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import static jdk.nashorn.internal.objects.NativeJava.type;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;

@Named(value = "attraction")
@Dependent
public class attraction {

    public String id;
    public String name;
    public String description;
    public String city;
    public String state;
    
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
        

    public attraction(String id, String name, String description,
            String city, String state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
    }

    public void mark(String state)
    {
        String mark;
        if (state.equals("approve"))
        {
            mark = "2";
        }
        else 
        {
            mark = "3";
        }
        
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {

            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select * from attractions "
                    + "WHERE att_id = '" + this.id + "'");
            while (rs.next()) {

                rs.updateString("status" , mark);
                rs.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        
        
        
    }
}
