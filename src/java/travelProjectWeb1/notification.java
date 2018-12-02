/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;
import static travelProjectWeb1.Login.getLogin;

/**
 *
 * @author raisa
 */
@Named(value = "notification")
@ManagedBean
public class notification {

    String ques;
    String ans;
    String q_useName;
    String a_userName;
    int att_id;
    int q_att_id;
    int Pending_Cnt;
    String att_Name;

    public int getPending_Cnt() {
        return Pending_Cnt;
    }

    public void setPending_Cnt(int Pending_Cnt) {
        this.Pending_Cnt = Pending_Cnt;
    }

    public int getQ_att_id() {
        return q_att_id;
    }

    public void setQ_att_id(int q_att_id) {
        this.q_att_id = q_att_id;
    }

    public String getAtt_Name() {
        return att_Name;
    }

    public void setAtt_Name(String att_Name) {
        this.att_Name = att_Name;
    }
  
    public int getAtt_id() {
        return att_id;
    }

    public void setAtt_id(int att_id) {
        this.att_id = att_id;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getQ_useName() {
        return q_useName;
    }

    public void setQ_useName(String q_useName) {
        this.q_useName = q_useName;
    }

    public String getA_userName() {
        return a_userName;
    }

    public void setA_userName(String a_userName) {
        this.a_userName = a_userName;
    }
    
    
    
    public notification() {
    }
    
    public notification(int cnt) {
        this.Pending_Cnt=cnt;
    }
    
    public notification(String ques,String ans, String a_userName, int att_id) 
    {
        this.ques=ques;
        this.ans=ans;
        this.a_userName=a_userName;
        this.att_id=att_id;
    }
    
    public notification(String att_Name,int att_id) 
    {
        this.att_Name=att_Name;
        this.att_id=att_id;
    }
    
    public ArrayList<notification> get_Notification()
    {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<notification> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select q.`ques`,a.`ans`,a.`userName`,q.att_ID from `att_question` q,`att_answer` a where q.`q_ID`=a.`q_ID` and a.`userName`!=q.`userName` and q.`userName`='"+getLogin().accountID+"' and `Q_A`=false");
            while (rs.next()) 
            {
                result.add(new notification(rs.getString(1), rs.getString(2),rs.getString(3),rs.getInt(4)));
            }
            rs=stat.executeQuery("SELECT `att_Name` FROM `attractions` WHERE `userName`='"+getLogin().accountID+"' and `status`=2 and `seen`=false");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result; 
        
    }
    
    public ArrayList<notification> get_Notification_att()
    {
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<notification> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            rs=stat.executeQuery("SELECT `att_ID`, `att_Name` FROM `attractions` WHERE `userName`='"+getLogin().accountID+"' and `status`=2 and `seen`=false");
            while (rs.next()) 
            {
                result.add(new notification(rs.getString(2),rs.getInt(1)));
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result; 
        
    }
    
    public ArrayList<notification> get_Notification_admin()
    {
        int cnt=0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<notification> result = new ArrayList<>();
        try {
            stat = conn.createStatement();
            rs=stat.executeQuery("SELECT count(*) FROM `attractions` WHERE `status`=1");
            while (rs.next()) 
            {
                result.add(new notification(rs.getInt(1)));;
            }
            
         
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result; 
        
    }
    
    public int getcnt()
    {
        int cnt=0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select q.`ques`,a.`ans`,a.`userName` from `att_question` q,`att_answer` a where q.`q_ID`=a.`q_ID` and a.`userName`!=q.`userName` and q.`userName`='"+getLogin().accountID+"' and `Q_A`=false");
            while (rs.next()) 
            {
                cnt+=1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return cnt; 
        
    }
    
    public String view_Att_Ans(int id) {
        int q=0;
        getLogin().att_id = id;
        int var=getLogin().att_id;
        userAccount1 u = new userAccount1(var);
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs=stat.executeQuery("select q_ID from att_question where att_ID='"+var+"' and ques='"+this.ques+"'");
            while(rs.next()){
                q=rs.getInt(1);
                int r=stat.executeUpdate("update att_answer set Q_A=true where q_ID='"+q+"'");
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "attraction.xhtml";
    }
    
    
    public String view_Att(int id) {
        int q=0;
        getLogin().att_id = id;
        int var=getLogin().att_id;
        userAccount1 u = new userAccount1(var);
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;
        try 
        {
            stat = conn.createStatement();
            int r=stat.executeUpdate("update attractions set seen=true where att_ID='"+var+"'");
          
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return "attraction.xhtml";
    }
    
}