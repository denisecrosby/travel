package travelProjectWeb1;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import static travelProjectWeb1.Login.*;

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
    protected boolean isAdmin = false;

    protected String searchCity = "";
    protected String searchTag = "";
    protected String searchName = "Name";

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }

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
            rs = stat.executeQuery("Select att_id, att_name, description, cityName, stateName "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, state, city c where a.state_id = state.sNum and a.city_id = c.cNum "
                    + " and s.statusNum = a.status and s.status = 'approved' and cityName like '%" + searchCity.trim() + "%' and att_name like '%" + (searchName.equals("Name") ? "" : searchName) + "%'"
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')) order by 6 desc");
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("cityName"), rs.getString("stateName"), rs.getString("favorite"), rs.getFloat("avg")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }

    public ArrayList<attraction> viewFavorites() {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<attraction> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from (Select att_id, att_name, description, cityName, stateName "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, state, city c where a.state_id = state.sNum and a.city_id = c.cNum "
                    + " and s.statusNum = a.status and s.status = 'approved' and cityName like '%" + searchCity.trim() + "%' and att_name like '%" + (searchName.equals("Name") ? "" : searchName) + "%'"
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')))a where favorite='true' order by 6 desc");
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("cityName"), rs.getString("stateName"), rs.getString("favorite"), rs.getFloat("avg")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }

    public ArrayList<attraction> youMayLike() {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<attraction> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from (Select a.att_id, att_name, description, cityName, stateName "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, state, city c, acc_tag act, attraction_tag att"
                    + " where a.state_id = state.sNum and a.city_id = c.cNum and act.username = '" + accountID + "' and att.att_ID = a.att_id and att.tag_ID = act.tag_id "
                    + " and s.statusNum = a.status and s.status = 'approved' "
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')))a where favorite='false' order by 6 desc LIMIT 0,3");
            while (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("cityName"), rs.getString("stateName"), rs.getString("favorite"), rs.getFloat("avg")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;

    }

    //this method is empty - a postback of the page causes the viewAttractions method to run and search with the specified city
    //this could be done better using javascript/refresh only part of the page
    public void searchAttractions() {

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public userAccount1(String accountId, String password) {
        this.accountID = accountId;
        this.password = password;
    }
}
