import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        var connectionUrl = "jdbc:postgresql://localhost/ovchip?user=postgres&password=hoi";
        try{
            Connection db = DriverManager.getConnection(connectionUrl);

            Statement st = db.createStatement();
            String query = "SELECT * FROM reiziger";

            ResultSet rs = st.executeQuery(query);

            String name;
            int i = 1;
            System.out.println("Alle reizigers:");
            while(rs.next()){
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                if(tussenvoegsel == null){
                    name = String.format("%s. %s", voorletters, achternaam);
                }else {
                    name = String.format("%s. %s %s", voorletters, tussenvoegsel, achternaam);
                }
                System.out.println(String.format("  #%d: %s", i, name));
                i += 1;
            }

            rs.close();
            st.close();
            db.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            System.err.println("[SQLException] Something went wrong with getting the SQL: " + e.getMessage());
        }
    }
}