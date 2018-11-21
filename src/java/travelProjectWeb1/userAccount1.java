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

/* 

    Authors    : raisa, denise
*/

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
    
    protected String searchCity;

    public String getAccountID() {
        return accountID;
    }

    public String getSearchCity() {
        return searchCity;
    }

    
    
    public void setSearchCity(String searchCity) {
        this.searchCity = searchCity;
    }

    public userAccount1(String searchCity) {
        this.searchCity = searchCity;
    }
    
    
    
    
    public ArrayList<attraction> viewAttractions() {

        Statement stat = null;
        ResultSet rs = null;

        ArrayList<attraction> result = new ArrayList<>();

        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select att_id,att_name,description,cityName,stateName,  "
                    + " (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + this.accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, state, city c where a.state_id = state.sNum and a.city_id = c.cNum "
                    + " and s.statusNum = a.status and s.status = 'approved'"
                            + " and cityName like '%" + this.searchCity + "%'"  );
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("cityName"), rs.getString("stateName"), rs.getString("favorite")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }

    public void searchAttractions() {
        
        
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
        this.searchCity = "";
    }

    public String returnToMain() {

        return ("welcome");

    }

}
