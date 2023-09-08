import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.*;

public class ReizigerDAOPsql implements ReizigerDAO{

    @Override
    public boolean save(Reiziger reiziger) {
        try{
            String query = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?);";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setString(5, reiziger.getGeboortedatum().toString());
            pst.executeQuery();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try{
            String query = "UPDATE reiziger SET voorletters=?, tussenvoegsel=?, achternaam=?, geboortedatum=? WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setString(4, reiziger.getGeboortedatum().toString());
            pst.setInt(5,reiziger.getId());
            pst.executeQuery();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try{
            String query = "DELETE reiziger WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,reiziger.getId());
            pst.executeQuery();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        return null;
    }
}
