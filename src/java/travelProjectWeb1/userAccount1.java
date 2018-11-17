package travelProjectWeb1;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.keyboard;
import static travelProjectWeb1.Login.openDatabase;

@Named(value = "userAccount1")
@SessionScoped
public class userAccount1 implements Serializable {

    protected String accountID;
    protected String password;
    protected String[] tags;
    protected String menu;
    //  protected ArrayList<attraction> youMayLike = new ArrayList<>();
    protected String[] favAttractions;
    protected String[] favCities;
    protected boolean isAdmin;

    public String getAccountID() {
        return accountID;
    }

    public ArrayList<attraction> viewAttractions() {

        Statement stat = null;
        ResultSet rs = null;

        ArrayList<attraction> result = new ArrayList<>();

        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select att_id,att_name,description,cityName,stateName from attractions a, status s, state, city c where a.state_id = state.sNum and a.city_id = c.cNum "
                    + " and s.statusNum = a.status");
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("cityName"), rs.getString("stateName")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public userAccount1(String accountId, String password) {
        this.accountID = accountId;
        this.password = password;
        //this.tags = tags;

        //check for unread answers 
        Connection conn0 = openDatabase();
        Statement stat0 = null;
        ResultSet rs0 = null;

        isAdmin = false;
    }

    public String returnToMain() {

        return ("welcome");

    }

}
