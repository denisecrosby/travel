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
public class userAccount1 implements Serializable{

    protected String accountID;
    protected String password;
    protected String[] tags;
    protected String menu;
  //  protected ArrayList<attraction> youMayLike = new ArrayList<>();
    protected String[] favAttractions;
    protected String[] favCities;

    public String getAccountID() {
        return accountID;
    }

    
    
    
    public userAccount1(String accountId, String password, String[] tags
            ,String[] favoriteattractions, String[] favoritecities) {
        this.accountID = accountId;
        this.password = password;
        this.tags = tags;
        
        //check for unread answers 
        Connection conn0 = openDatabase();
        Statement stat0 = null;
        ResultSet rs0 = null;

        try {
            stat0 = conn0.createStatement();
            rs0 = stat0.executeQuery("Select distinct q.attractionName from "
                    + "answer a,question q where a.questionid = q.questionID "
                    + "and isRead = 0 and q.accountid = '" + this.accountID + "'");
            while (rs0.next()) {
                out.println("NOTICE: You have an unread answer "
                        + "for attraction: " + rs0.getString("attractionName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs0, stat0, conn0);
        }
        
        //string.join will enter a new line with each comma
        this.menu = String.join("\n",
                "Please make your selection",
                "1: Submit a request for an attraction.",
                "2: Rate an attraction",
                "3: Search attractions",
                "4: Enter favorite attractions",
                "5: Enter favorite cities"
        );
        this.favAttractions = favoriteattractions;
        this.favCities = favoritecities;

        
        //you may like
        Statement stat = null;
        ResultSet rs = null;

        Connection conn = openDatabase();
        try {
            stat = conn.createStatement();
            
            rs = stat.executeQuery(
                    "select * from "
                    + "(select name, description, city, state, tags, "
                            + "(select truncate(coalesce(sum(reviewScore)/"
                            + "count(reviewScore),0),1) from attractionreview "
                            + "where attractionName = a.name) as avg "
                    + "from (select name,description, city, state, tags from "
                            + "attraction where " + Arrays.stream(tags)
                                    .map(x -> "tags like '%" + x + "%'")
                                    .collect(Collectors.joining(" OR "))
                    + " )a )a order by 6 desc LIMIT 0,3");

     /*       while (rs.next()) {
                youMayLike.add(new attraction(rs.getString("Name")
                        , rs.getString("description")
                        , rs.getString("city")
                        , rs.getString("state")
                        , rs.getString("tags").split("#", 0)));
                youMayLike.get(youMayLike.size() - 1).avg = rs.getDouble("avg");
            }  */

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase(rs, stat, conn);
        }

        int index = 97;
   /*     for (attraction x : youMayLike) {
            menu += "\n" + ((char) index++) 
                    + ": You may like " + x.name + " (" + x.avg + ")";  */
        }
    }

//    public void displayMenu() {
//        while (true) {
//            out.println(menu);
//            out.println("x: Logout");
//
//            switch (keyboard.nextLine()) {
//                case "1":
//                    submitRequest();
//                    break;
//                case "2":
//                    rateAttraction();
//                    break;
//                case "3":
//                    searchAttractions();
//                    break;
//                case "4":
//                    editFavorites("attractions");
//                    break;
//                case "5":
//                    editFavorites("cities");
//                    break;
//                case "a":
//                    if (!youMayLike.isEmpty()) {
//                        viewAttraction(youMayLike.get(0));
//                    }
//                    break;
//                case "b":
//                    if (youMayLike.size() > 1) {
//                        viewAttraction(youMayLike.get(1));
//                    }
//                    break;
//                case "c":
//                    if (youMayLike.size() == 3) {
//                        viewAttraction(youMayLike.get(2));
//                    }
//                    break;
//                case "x":
//                    return;
//            }
//        }
//    }

//    protected void editFavorites(String type) {
//        out.println("Enter all your favorite " 
//                + type + " (comma separated list)");
//        String favorites = String.join("#", keyboard.nextLine().toLowerCase()
//                .replaceAll(", ", ",").split(","));
//        if (type.equals("attractions")) {
//            this.favAttractions = favorites.split("#");
//        } else {
//            this.favCities = favorites.split("#");
//        }
//
//        Connection conn = openDatabase();
//        Statement stat = null;
//        ResultSet rs = null;
//        try {
//
//            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
//                    , ResultSet.CONCUR_UPDATABLE);
//            rs = stat.executeQuery("select * from travelaccount "
//                    + "WHERE accountID = '" + accountID + "'");
//            while (rs.next()) {
//
//                rs.updateString("favorite" + type, favorites);
//                rs.updateRow();
//                System.out.println("Favorite " + type + " saved");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(rs, stat, conn);
//        }
//    }
//
//    protected void searchAttractions() {
//        out.println("What do you want to search by? "
//                + "After selecting, you will be able to search favorites.");
//        out.println("1: Tag");
//        out.println("2: City");
//        out.println("x: Return to menu");
//
//        String search;
//        String filter;
//        switch (keyboard.nextLine()) {
//            case "1":
//                search = "tags";
//                out.println("Enter a tag for searching: ");
//                filter = keyboard.nextLine();
//                break;
//            case "2":
//                search = "city";
//                out.println("Enter the city for searching: ");
//                filter = keyboard.nextLine();
//                break;
//            case "x":
//                return;
//            default:
//                searchAttractions();
//                return;
//        }
//
//        boolean favorites = false;
//        while (true) {
//            out.println("Would you like to use your "
//                    + "favorites to search? [YES, NO]");
//            String next = keyboard.nextLine();
//            if (next.toUpperCase().equals("NO")) {
//                break;
//            }
//            if (next.toUpperCase().equals("YES")) {
//                favorites = true;
//                break;
//            }
//        }
//
//        Statement stat = null;
//        ResultSet rs = null;
//        ArrayList<String> searchList = new ArrayList<>();
//        ArrayList<Float> scoreList = new ArrayList<>();
//
//        Connection conn = openDatabase();
//
//        try {
//            stat = conn.createStatement();
//
//            //example string format
//            //String.format("hello %s", "world") -> "hello world"
//            // find all user's favorite attractions where tag contains selected tag
//            rs = stat.executeQuery(String.format(
//                    "select name, city, (select truncate(coalesce(sum(reviewScore)"
//                        + "/count(reviewScore),0),1) from attractionreview "
//                        + "where attractionName = a.name) as avg "
//                    + "from (Select name, city from attraction where %s "
//                        + "like '%%%s%%') as a order by avg desc"
//                    , search, filter));
//
//            while (rs.next()) {
//                if (favorites) {
//                    if (search.equals("tags")) {
//                        if (Arrays.toString(favAttractions).toLowerCase()
//                                .contains(rs.getString("name").toLowerCase())) {
//                            searchList.add(rs.getString("Name"));
//                            scoreList.add(rs.getFloat("avg"));
//                        }
//                    } else {
//                        if (Arrays.toString(favCities).toLowerCase()
//                                .contains(rs.getString("city").toLowerCase())) {
//                            searchList.add(rs.getString("Name"));
//                            scoreList.add(rs.getFloat("avg"));
//                        }
//                    }
//                } else {
//                    searchList.add(rs.getString("Name"));
//                    scoreList.add(rs.getFloat("avg"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(rs, stat, conn);
//        }
//
//        String thisName;
//
//        if (searchList.isEmpty()) {
//            out.println("No attractions found");
//            return;
//        }
//
//        for (int count = 0; count < searchList.size(); count++) {
//            out.println(String.format("%s: %s Average Rating: %s"
//                    , count + 1, searchList.get(count), scoreList.get(count)));
//        }
//
//        while (true) {
//            out.println("Select an attraction to view:");
//            try {
//                thisName = searchList.get
//                    (Integer.valueOf(keyboard.nextLine()) - 1);
//                break;
//            } catch (Exception e) {
//
//            }
//        }
//        viewAttraction(attraction.getAttraction(thisName));
//    }
//
//    protected void viewAttraction(attraction a) {
//        out.println(a.name.replace("\\", ""));
//        out.println(a.description);
//        out.println(a.city);
//        out.println(a.state);
//        out.println(Arrays.toString(a.tags));
//        out.printf("Average Score: %.1f\n", a.avgScore());
//        
//        //display reviews related to this attraction
//        ArrayList<review> rList = review.getReview(a.name);
//        if (rList.isEmpty()) {
//            out.println("No reviews");
//
//        } else {
//            rList.forEach(out::println);
//        }
//        
//        //display questions related to this attraction
//        ArrayList<question> qList = question.getQuestion(a.name);
//        if (qList.isEmpty()) {
//            out.println("No questions");
//        } else {
//            for (int i = 0; i < qList.size(); i++) {
//              //  out.println("Qlist size: " + qList.size());
//                out.println("Q: " + qList.get(i));
//
//                answer.getAnswer(qList.get(i).getQuestionID())
//                        .forEach(x -> out.println("A: " + x));
//                
//                //if this is the person who asked the question
//                //, update answers to read
//                if (qList.get(i).getAccountID().equals(this.accountID)) {
//
//                    Statement stat = null;
//                    ResultSet rs = null;
//
//                    Connection conn = openDatabase();
//                    try {
//                        stat = conn.createStatement
//                  (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                        rs = stat.executeQuery("Select * from answer "
//                                + "where questionID = '" 
//                                + qList.get(i).getQuestionID() + "'");
//                        while (rs.next()) {
//                            rs.updateString("isRead", "1");
//                            rs.updateRow();
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    } finally {
//                        closeDatabase(rs, stat, conn);
//                    }
//                }
//
//                while (true) {
//                    out.println("Would you like to "
//                            + "answer this question? [YES, NO]");
//                    String next = keyboard.nextLine();
//                    if (next.toUpperCase().equals("NO")) {
//                        break;
//                    }
//                    if (next.toUpperCase().equals("YES")) {
//                        out.println("What is your answer?");
//                        String answ = keyboard.nextLine();
//                        if (!answ.isEmpty()) {
//                            answer.postAnswer(answ, 
//                                    qList.get(i).getQuestionID());
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//
//        while (true) {
//            out.println("Would you like to enter a question? [YES, NO]");
//            String next = keyboard.nextLine();
//            if (next.toUpperCase().equals("NO")) {
//                break;
//            }
//            if (next.toUpperCase().equals("YES")) {
//                out.println("What is your question?");
//                String quest = keyboard.nextLine();
//                if (!quest.isEmpty()) {
//                    question.createQuestion(quest, a.name, accountID);
//                }
//                break;
//            }
//        }
//    }
//
//    protected void rateAttraction() {
//        out.println("Enter the attraction name: ");
//        String name = keyboard.nextLine().replace("'", "\\'");
//
//        if (attraction.getAttraction(name) == null) {
//            return;
//        }
//
//        out.println("Enter the text for your review");
//        String description = keyboard.nextLine().replace("'", "\\'");
//
//        byte rating = 0;
//        while (rating < 1 || rating > 5) {
//            out.println("Enter a star rating for your review (1-5)");
//            try
//            {
//                rating = Byte.parseByte(keyboard.nextLine());
//            }
//            catch (NumberFormatException e)
//            {
//                out.println("Input must be in number form");
//            }
//        }
//
//        review.postReview(name, accountID, description, rating);
//    }
//
//    protected void submitRequest() {
//        out.println("Enter the attraction name: ");
//        String name = keyboard.nextLine().replace("'", "\\'");
//        out.println("Enter the description (1000 characters max): ");
//        String desc = keyboard.nextLine().replace("'", "\\'");
//        out.println("Enter the city: ");
//        String city = keyboard.nextLine();
//        out.println("Enter the state abbreviation (2 characters max): ");
//        String state;
//        try {
//            state = keyboard.nextLine().substring(0, 2).toUpperCase();
//        } catch (Exception e) {
//            state = "TX"; // default
//        }
//
//        String rTags = getTags();
//
//        Connection conn = openDatabase();
//        Statement stat = null;
//
//        try {
//            stat = conn.createStatement();
//            try {
//                stat.executeUpdate(String.format("Insert into attractionrequest"
//                        + " (name, description, city, state, tags) "
//                        + "values('%s', '%s', '%s', '%s', '%s')"
//                        , name, desc, city, state, rTags));
//                out.println("The attraction request is created!");
//            } catch (SQLException e) {
//                System.err.println("Attraction already "
//                        + "in database or too many letters entered!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(null, stat, conn);
//        }
//    }
        
         
        
//} 
    


