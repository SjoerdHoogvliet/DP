import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements  OVChipkaartDAO{

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try{
            String query = "INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?);";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger_id());
            //pst.setInt(6, ovChipkaart.getReiziger().getId());
            pst.execute();
            pst.close();


            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at save for OVChipkaartDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try{
            String query = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=?, ov_chipkaart_reiziger_id_fkey=? WHERE kaart_nummer=?;";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setDate(1, ovChipkaart.getGeldigTot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger_id());
            pst.setInt(5, ovChipkaart.getReiziger().getId());
            pst.setInt(6, ovChipkaart.getKaartNummer());
            pst.execute();
            pst.close();

            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at update for OVChipkaartDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try{
            String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1,ovChipkaart.getKaartNummer());
            pst.execute();
            pst.close();

            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at delete for OVChipkaartDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id=?";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            List<OVChipkaart>OVChipkaartList = new ArrayList<>();
            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                int reiziger_id = rs.getInt("reiziger_id");

                OVChipkaart ovck = new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger_id);
                OVChipkaartList.add(ovck);
            }

            return OVChipkaartList;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at findByReiziger for OVChipkaartDAOPsql: " + sqle.getMessage());
            return null;
        }
    }

    public List<OVChipkaart> findAll(){
        try{
            String query = "SELECT * FROM ov_chipkaart";
            PreparedStatement pst = Main.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            List<OVChipkaart>OVChipkaartList = new ArrayList<>();
            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                int reiziger_id = rs.getInt("reiziger_id");

                OVChipkaart ovck = new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger_id);
                OVChipkaartList.add(ovck);
            }

            return OVChipkaartList;

        } catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at findAll for OVChipkaartDAOPsql: " + sqle.getMessage());
            return null;
        }
    }
}
