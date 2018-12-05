package travelProjectWeb1;

import java.io.ByteArrayInputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import static travelProjectWeb1.Login.*;

/* 
    Authors    : raisa, denise
 */
@Named(value = "userAccount1")
@SessionScoped
public class userAccount1 implements Serializable {

    protected String accountID;
    protected int att_Id;
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
    protected String review;
    protected int att_id;
    protected attraction currentAtt;
    private StreamedContent productImage;
    protected String answer;
    protected notification notify;

    public notification getNotify() {
        return notify;
    }

    public void setNotify(notification notify) {
        this.notify = notify;
    }

    public attraction getCurrentAtt() {
        return currentAtt;
    }

    public void setCurrentAtt(attraction currentAtt) {
        this.currentAtt = currentAtt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public StreamedContent getProductImage() {
        return productImage;
    }

    public void setProductImage(StreamedContent productImage) {
        this.productImage = productImage;
    }

    public int getAtt_id() {
        return att_id;
    }

    public void setAtt_id(int att_id) {
        this.att_id = att_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getAtt_Id() {
        return att_Id;
    }

    public void setAtt_Id(int att_Id) {
        this.att_Id = att_Id;
    }

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

    public String getPassword() {
        return password;
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
    
    public ArrayList<userAccount1> viewProfile()
    {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<userAccount1> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from account where userName='" + accountID+ "' and password='"+password+"'");
            while (rs.next()) {
                byte[] localimage = rs.getBytes(3);
                StreamedContent sc = new DefaultStreamedContent(new ByteArrayInputStream(localimage));
                result.add(new userAccount1(rs.getString(1), rs.getString(2),sc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;
    }
    
   

    public ArrayList<attraction> viewAttractions() {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        Connection conn1 = openDatabase();
        Statement stat1 = null;
        ResultSet rs1 = null;
        ArrayList<attraction> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            PreparedStatement stmt = null;
            rs = stat.executeQuery("Select att_id, att_name, description, c.city, state "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, att_state, att_city c where a.state_id = att_state.state_id and a.city_id = c.city_id "
                    + " and s.statusNum = a.status and s.status = 'approved' and c.city like '%" + searchCity.trim() + "%' and att_name like '%" + (searchName.equals("Name") ? "" : searchName) + "%'"
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')) order by 6 desc");
            while (rs.next()) {
                int a = rs.getInt(1);
                stmt = conn1.prepareStatement("select * from attractions where att_id=?");
                stmt.setString(1, Integer.toString(a));
                rs1 = stmt.executeQuery();
                if (rs1.next()) {
                    byte[] localimage = rs1.getBytes("att_Img");
                    StreamedContent sc = new DefaultStreamedContent(new ByteArrayInputStream(localimage));
                    result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("city"), rs.getString("state"), rs.getString("favorite"), rs.getFloat("avg"),
                            sc));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
            closeDatabase(rs1, stat1, conn1);
        }
        return result;
    }

    public ArrayList<attraction> viewTopAttractions() {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<attraction> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from (Select att_id, att_name, description, c.city, state "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, att_state, att_city c where a.state_id = att_state.state_id and a.city_id = c.city_id "
                    + " and s.statusNum = a.status and s.status = 'approved')a "
                    + " where avg>4"
                    + " order by 6 desc");
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

    public ArrayList<attraction> viewa() {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<attraction> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            int a = getLogin().att_id;
            rs = stat.executeQuery("Select att_id, att_name, description, c.city, state"
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, att_state, att_city c where a.att_id='" + a + "' and a.state_id = att_state.state_id and a.city_id = c.city_id ");
            if (rs.next()) {
                result.add(new attraction(rs.getString("att_id"), rs.getString("att_name"), rs.getString("description"), rs.getString("city"), rs.getString("state"), rs.getString("favorite"), rs.getFloat("avg")));
            }
            this.productImage = null;

            PreparedStatement stmt = null;
            byte[] localImage = null;
            try {
                stmt = conn.prepareStatement("select * from attractions where att_id=?");
                stmt.setString(1, Integer.toString(a));
                rs = stmt.executeQuery();

                if (rs.next()) {
                    localImage = rs.getBytes("att_Img");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.productImage = new DefaultStreamedContent(new ByteArrayInputStream(localImage));

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
            rs = stat.executeQuery("select * from (Select att_id, att_name, description, c.city, state "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, att_state, att_city c where a.state_id = att_state.state_id and a.city_id = c.city_id "
                    + " and s.statusNum = a.status and s.status = 'approved' and c.city like '%" + searchCity.trim() + "%' and att_name like '%" + (searchName.equals("Name") ? "" : searchName) + "%'"
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')))a where favorite='true' order by 6 desc");
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

    public ArrayList<Review> viewReview() {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<Review> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_review where att_ID='" + getLogin().att_id + "'");
            while (rs.next()) {
                result.add(new Review(rs.getString(3), rs.getString(4)));
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
            rs = stat.executeQuery("select * from (Select a.att_id, att_name, description, c.city, state "
                    + ", (select truncate(coalesce(sum(score)/count(score),0),1) from att_score where att_ID = a.att_id) as avg  "
                    + ", (case when exists(select att_id from myfavoritedes f where f.att_id = a.att_id and userName = '" + accountID + "') then 'true' else 'false' END) as favorite "
                    + " from attractions a, status s, att_state, att_city c, acc_tag act, attraction_tag att"
                    + " where a.state_id = att_state.state_id and a.city_id = c.city_id and act.username = '" + accountID + "' and att.att_ID = a.att_id and att.tag_ID = act.tag_id "
                    + " and s.statusNum = a.status and s.status = 'approved' "
                    + " and exists (select 1 from attraction_tag where att_ID = a.att_id and tag_ID in (select tag_id from tags where tag like '%" + searchTag.trim() + "%')))a where favorite='false' order by 6 desc LIMIT 0,3");
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

    //the searchAttractions is empty - a postback of the page causes the viewAttractions method to run and search with the specified city
    //this could be done better using javascript/refresh only part of the page
    public void searchAttractions() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public userAccount1(String accountId, String password) {
        this.accountID = accountId;
        this.password = password;
        this.notify = new notification();
    }
    
    public userAccount1(String accountId, String password,StreamedContent image) {
        this.accountID = accountId;
        this.password = password;
        this.productImage=image;
    }
    
    public userAccount1(int att_Id) {
        this.att_Id = att_Id;
    }
}
