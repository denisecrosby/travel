package travelProjectWeb1;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/* Authors    : raisa, denise  */
@ManagedBean
@SessionScoped
//sessionScoped need to implement the interface Serializable
public class Login implements Serializable {

    //attributes
    public static final Scanner keyboard = new Scanner(System.in);
    public static final String URL = "jdbc:mysql://mis-sql.uhcl.edu/antaor0966";
    public static final List<String> validTags = Arrays.asList("history", "shopping", "beach", "urban", "explorer", "nature", "family");
    private String id;
    private String password;
    private static userAccount1 theLoginAccount;
    private state st;
    private city ct;

    //get methods and set methods
    public String getId() {
        return id;
    }

    public userAccount1 getTheLoginAccount() {
        return theLoginAccount;
    }

    public static void setTheLoginAccount(userAccount1 theLoginAccount) {
        Login.theLoginAccount = theLoginAccount;
    }

    public static userAccount1 getLogin() {
        return theLoginAccount;
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

    public state getSt() {
        return st;
    }

    public city getCt() {
        return ct;
    }

    public void setSt(state st) {
        this.st = st;
    }

    public void setCt(city ct) {
        this.ct = ct;
    }

    public String login() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            //return to internalError.xhtml
            return ("internalError");
        }

        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        StreamedContent image = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from account where userName = '" + id + "' and password = '" + password + "'");

            if (rs.next()) {
                st = new state();
                ct = new city();
                if (id.equals("admin")) {
                    this.theLoginAccount
                            = new adminAccount(id, password);
                } else {
                    this.theLoginAccount
                            = new userAccount1(id, password);
                }
                return "welcome";
            } else {
                id = "";
                password = "";
                return "loginNotOK";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ("internalError");
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }

    public static Connection openDatabase() {
        try {
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
