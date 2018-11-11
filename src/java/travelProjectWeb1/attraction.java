/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelProjectWeb1;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author denisecrosby
 */
@Named(value = "attraction")
@Dependent
public class attraction {

    public String name;
    public String description;
    public String city;
    public String state;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public attraction(String name, String description,
            String city, String state) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
    }

//    public static attraction getAttraction(String name) {
//        Connection conn = openDatabase();
//        Statement stat = null;
//        ResultSet rs = null;
//        attraction result = null;
//        try {
//            stat = conn.createStatement();
//            rs = stat.executeQuery("Select att_name,description,cityName,stateName from attraction a, state s, city c where a.state_id = s.sNum and a.city_id = c.cNum"
//                    + " and name = '" + name.replace("'", "\\'") + "'");
//            if (rs.next()) {
//                result = new attraction(
//                        name,
//                        rs.getString("description"),
//                        rs.getString("city"),
//                        rs.getString("state"),
//                        rs.getString("tags").split("#")
//                );
//            } else {
//                out.println("That attraction dosen't exist "
//                        + "or is not yet approved");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(rs, stat, conn);
//        }
//        return result;
//    }
//
//    public static void createAttraction(String name, String description,
//            String city, String state, String[] tags) {
//        Connection conn = openDatabase();
//        Statement stat = null;
//
//        try {
//            stat = conn.createStatement();
//            stat.executeUpdate(
//                    String.format("Insert into attraction "
//                            + "(name, description, city, state, tags)"
//                            + " values('%s', '%s', '%s', '%s', '%s')",
//                            name.replace("'", "\\'"),
//                            description.replace("'", "\\'"),
//                            city,
//                            state,
//                            String.join("#", tags)
//                    )
//            );
//            out.println("The attraction is created!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(null, stat, conn);
//        }
//    }
//
//    public double avgScore() {
//        Statement stat = null;
//        ResultSet rs = null;
//
//        Connection conn = openDatabase();
//        try {
//            stat = conn.createStatement();
//
//            //  out.println("select truncate(coalesce(sum(reviewScore)/count(reviewScore),0),1) from attractionreview where attractionName = '" + this.name + "'");
//            rs = stat.executeQuery("select "
//                    + "truncate(coalesce(sum(reviewScore)/count(reviewScore),0),1)"
//                    + "from attractionreview where attractionName = '" + this.name.replace("'", "\\'")
//                    + "'");
//            if (rs.next()) {
//                return rs.getFloat(1);
//            } else {
//                return 0.0;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDatabase(rs, stat, conn);
//        }
//
//        return 0.0;
//    }
}
