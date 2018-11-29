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
import static travelProjectWeb1.Login.getLogin;
import static travelProjectWeb1.Login.openDatabase;

/**
 *
 * @author raisa
 */
@Named(value = "question_Answers")
@ManagedBean
public class Question_Answers {

    String userName_Q;
    String userName_A;
    String ques;
    String ans;
    int ques_id;

    public String getUserName_Q() {
        return userName_Q;
    }

    public void setUserName_Q(String userName_Q) {
        this.userName_Q = userName_Q;
    }

    public String getUserName_A() {
        return userName_A;
    }

    public void setUserName_A(String userName_A) {
        this.userName_A = userName_A;
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

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
    
    
    public Question_Answers() {
    }
    
    public Question_Answers(int ques_id,String userName_Q,String ques) {
        this.ques_id=ques_id;
        this.userName_Q=userName_Q;
        this.ques=ques;
    }
    
    public Question_Answers(String userName_A,String ans) {
        this.userName_A=userName_A;
        this.ans=ans;
    }
    
    public void add_Question(int att_id,String un)
    {
        int max = 0;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            
            stat = conn.createStatement();
            rs=stat.executeQuery("select max(q_ID) from att_question");
            while(rs.next())
            {
                max=rs.getInt(1)+1;
            }
            if(!ques.equals(null)){
                stat.executeUpdate("insert into att_question values('" + max + "','" + att_id + "','" + ques + "','" + un + "')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
    
    public ArrayList<Question_Answers> view_Question()
    {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<Question_Answers> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_question where att_ID='"+getLogin().att_id+"'");
            while(rs.next()){
                result.add(new Question_Answers(rs.getInt(1),rs.getString(4),rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;
    }
    
     public void add_Answer(String un)
    {
        int max = 0;
        Connection c = openDatabase();
        Statement s = null;
        ResultSet r = null;

        try {
            
            s = c.createStatement();
            if(!getLogin().answer.equals(null)){
                s.executeUpdate("insert into att_answer values('" + this.ques_id + "','"+ getLogin().answer+"',false,'" + un + "')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(r, s, c);
            getLogin().answer = null;
        }
    }
    
    public ArrayList<Question_Answers> view_Answer()
    {
        Statement stat = null;
        ResultSet rs = null;
        ArrayList<Question_Answers> result = new ArrayList<>();
        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from att_answer where q_ID='"+this.ques_id+"'");
            while(rs.next()){
                result.add(new Question_Answers(rs.getString(4),rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        return result;
    }
}
