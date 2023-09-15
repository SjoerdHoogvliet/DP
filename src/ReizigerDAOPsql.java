import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ReizigerDAOPsql implements ReizigerDAO{
//    private Connection conn = Main.connection;
    private AdresDAO adao = new AdresDAOPsql();

    @Override
    public boolean save(Reiziger reiziger) {
        try{
            String query = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?);";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at save: " + sqle.getMessage());
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
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.setInt(5,reiziger.getId());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try{
            String query = "DELETE FROM reiziger WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,reiziger.getId());
            pst.execute();
            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        try{
            String query = "SELECT * FROM reiziger WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            String voorletters = null;
            String tussenvoegsel = null;
            String achternaam = null;
            Date geboortedatum = null;
            while(rs.next()){
                voorletters = rs.getString("voorletters");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                geboortedatum = rs.getDate("geboortedatum");
            }

            Reiziger r = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
            r.setAdres(adao.findByReiziger(r));
            return r;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        List<Reiziger> reizigerList = new ArrayList<>();
        try{
            String query = "SELECT * FROM reiziger WHERE geboortedatum=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Reiziger r = new Reiziger(id, voorletters, tussenvoegsel, achternaam, Date.valueOf(datum));
                r.setAdres(adao.findByReiziger(r));
                reizigerList.add(r);
            }

            return reizigerList;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL: " + sqle.getMessage());
            return reizigerList;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigerList = new ArrayList<>();
        try{
            String query = "SELECT * FROM reiziger";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                Reiziger r = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                r.setAdres(adao.findByReiziger(r));
                reizigerList.add(r);
            }

            return reizigerList;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at findAll(): " + sqle.getMessage());
            return reizigerList;
        }
    }
}
