package travelProjectWeb1;

import java.sql.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import static travelProjectWeb1.Login.*;

@ManagedBean
@RequestScoped
public class ResetPassword {

    private String userName;
    private String question;
    private String ansGiven;
    private String errorMessage;
    private String successMessage;
    private Boolean flag = false;
    private String newPassword;
    private Boolean questionFlag = true;
    private static String id;

    public Boolean getQuestionFlag() {
        return questionFlag;
    }

    public void setQuestionFlag(Boolean questionFlag) {
        this.questionFlag = questionFlag;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getErrorMessage() {

        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getAnsGiven() {
        return ansGiven;
    }

    public void setAnsGiven(String ansGiven) {
        this.ansGiven = ansGiven;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSecurityQuesAndAnswer() {
        this.successMessage = null;
        this.errorMessage = null;
        this.flag = null;
        id = null;
        this.newPassword = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return ("internalError");
        }
        String path = "";
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("SELECT userName from account where userName='" + userName + "'");

            if (rs.next()) {

            } else {
                this.errorMessage = "username not exist";
                return "username not exist";
            }

            rs = stat.executeQuery("SELECT aq.ques,a.ans FROM acc_question aq inner join account a on "
                    + " a.ques_id=aq.ques_id where a.username='" + userName + "'");

            while (rs.next()) {
                this.question = rs.getString("ques");
                this.id = userName;
                this.questionFlag = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        System.out.println("User ID:" + userName);
        return path;
    }

    public String checkAnswer() {
        String path = "";
        System.out.println("UserName:" + id);
        String correctAnswer = getAnswerForUserId(ResetPassword.id);
        System.out.println("Answer Given:" + ansGiven);
        System.out.println("correctAnswer:" + correctAnswer);
        if (ansGiven.equals(correctAnswer)) {
            flag = true;

        } else {
            flag = false;
            this.errorMessage = "Given Answer is not Correct";
        }
        return path;
    }

    public String updatePassword() {
        String path = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return ("internalError");
        }
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = conn.createStatement();
            System.err.println("new Password:" + newPassword);
            System.out.println("ID:" + id);
            stat.executeUpdate("update account set password='" + newPassword + "' where username='" + id + "'");
            this.successMessage = "Password Updated Successfully";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }

        return path;
    }

    public String getAnswerForUserId(String userid) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return ("internalError");
        }
        String answer = "";
        Connection conn = openDatabase();
        Statement stat = null;
        ResultSet rs = null;

        try {
            System.out.println("Id:" + id);
            stat = conn.createStatement();
            rs = stat.executeQuery("SELECT a.ans FROM acc_question aq inner join account a on "
                    + " a.ques_id=aq.ques_id where a.username='" + userid + "'");

            while (rs.next()) {
                answer = rs.getString("ans");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }
        System.out.println("Answer from DB:" + answer);
        return answer;
    }

}
