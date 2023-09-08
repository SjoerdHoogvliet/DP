import java.sql.*;
import java.util.List;

public class Main {
    static Connection connection;
    public static void main(String[] args){
        try{ReizigerDAO rdao = new ReizigerDAOPsql();
            Main.getConnection();
            Main.testReizigerDAO(rdao);
            Main.closeConnection();
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with getting the SQL: " + sqle.getMessage());
        }
    }
    private static void getConnection() {
        try{
            String connectionUrl = "jdbc:postgresql://localhost/ovchip?user=postgres&password=hoi";
            connection = DriverManager.getConnection(connectionUrl);
        } catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with opening the connection: " + sqle.getMessage());
        }
    }

    private static void closeConnection(){
        try{
            connection.close();
        } catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with closing the connection: " + sqle.getMessage());
        }
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}