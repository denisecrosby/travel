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
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import static travelProjectWeb1.Login.closeDatabase;
import static travelProjectWeb1.Login.openDatabase;

/**
 *
 * @author raisa
 */
@Named(value = "score")
@ManagedBean
public class Score {

    int scr=0;

    public int getScr() {
        return scr;
    }

    public void setScr(int scr) {
        this.scr = scr;
    }
    
    public Score()
    {
    }
    
    public void addScore(int att_id,String un)
    {
        boolean exist = false;
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            
            stat = conn.createStatement();
            rs=stat.executeQuery("select * from att_score where att_ID='"+att_id+"' and userName='"+un+"'");
            while(rs.next()){
                exist=true;
            }
            if(exist==true){
                FacesMessage message = new FacesMessage("You can only score the attraction once");
                FacesContext.getCurrentInstance().addMessage("att_form:att_scr", message);
            }
            else
            {
                stat.executeUpdate("insert into att_score values('" + att_id + "','" + scr + "','" + un + "')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
    }
    
}
