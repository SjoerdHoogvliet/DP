import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
//    private Connection conn = Main.connection;
    private ReizigerDAO rdao;

    @Override
    public boolean save(Adres adres) {
        try{
            String query = "INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?);";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger_id());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at save: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try{
            String query = "UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=? WHERE adres_id = ?;";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger_id());
            pst.setInt(6,adres.getId());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at save: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try{
            String query = "DELETE FROM adres WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,adres.getId());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override public Adres findById(int id) {
        try{
            String query = "SELECT * FROM adres WHERE adres_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            String postcode = null;
            String huisnummer = null;
            String straat = null;
            String woonplaats = null;
            int reiziger_id = 0;
            while(rs.next()){
                postcode = rs.getString("postcode");
                huisnummer = rs.getString("huisnummer");
                straat = rs.getString("straat");
                woonplaats = rs.getString("woonplaats");
                reiziger_id = rs.getInt("reiziger_id");
            }

            Adres a = new Adres(id, postcode, huisnummer, straat, woonplaats, reiziger_id);
            return a;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try{
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            String postcode = null;
            String huisnummer = null;
            String straat = null;
            String woonplaats = null;
            int adres_id = 0;
            while(rs.next()){
                postcode = rs.getString("postcode");
                huisnummer = rs.getString("huisnummer");
                straat = rs.getString("straat");
                woonplaats = rs.getString("woonplaats");
                adres_id = rs.getInt("adres_id");
            }
            if(postcode == null && huisnummer == null && straat == null && woonplaats == null) {
                return null;
            }
            else {
                return new Adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger.getId());
            }
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        List<Adres> adresList = new ArrayList<>();
        try{
            String query = "SELECT * FROM adres";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                int id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                int reiziger_id = rs.getInt("reiziger_id");

                Adres a = new Adres(id, postcode, huisnummer, straat, woonplaats, reiziger_id);
                adresList.add(a);
            }

            return adresList;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at findAll(): " + sqle.getMessage());
            return adresList;
        }
    }
}
