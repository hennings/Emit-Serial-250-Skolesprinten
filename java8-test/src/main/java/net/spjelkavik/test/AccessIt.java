package net.spjelkavik.test;


import java.sql.*;

public class AccessIt {

    public static void main(String[] a) {

        try {

            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://c:/usr/arr/test/etime.mdb;memory=false");
            PreparedStatement ps2;
            PreparedStatement ps;
            ps = conn.prepareStatement("select id,name,ename,ecard from name where id>725");

            listRunners(ps);

            ps2 = conn.prepareStatement("update name set ecard='" + ((int) Math.random()*400000)+"' where id = 727");
            ps2.executeUpdate();

            listRunners(ps);

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private static void listRunners(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(": " + rs.getObject(1)
                    +"\t" + rs.getObject(2)
                    +"\t" + rs.getObject(3)
                    +"\t" + rs.getObject(4)
            );
        }
    }

}