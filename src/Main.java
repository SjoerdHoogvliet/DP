import java.sql.*;
import java.util.List;

public class Main {
    static Connection connection;
    public static void main(String[] args){
        try{
            ReizigerDAO rdao = new ReizigerDAOPsql();
            AdresDAO adao = new AdresDAOPsql();
            Main.getConnection();
            Main.testReizigerDAO(rdao);
            Main.testAdresDao(adao, rdao);
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
        System.out.println(reizigers.size() + " reizigers");

        //Vind alle reizigers met dezelfde geboortedatum als S. Boers
        List<Reiziger> foundByGb = rdao.findByGbdatum(gbdatum);
        System.out.println("[TEST] De volgende reizigers zijn gevonden met ReizigerDAO.findByGbdatum():");
        for(Reiziger r : foundByGb){
            System.out.println(r.toString());
        }

        //Vind de nieuwe reiziger in de database met zijn id
        int id = 77;
        Reiziger foundById = rdao.findById(id);
        System.out.println(String.format("[TEST] Gevonden door het zoeken naar id %s: %s", id, foundById));

        //Wijzig de zojuist met id gevonden reiziger in de database
        System.out.println("[TEST] Eerst: " + foundById.toString());
        Reiziger exampleReiziger = new Reiziger(77, "A", "", "Broers", Date.valueOf("1971-12-03"));
        rdao.update(exampleReiziger);
        Reiziger updatedReiziger = rdao.findById(id);
        System.out.println("na ReizigerDAO.update(): " + updatedReiziger.toString());

        //Verwijder de nieuwe reiziger
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(exampleReiziger);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testAdresDao(AdresDAO adao, ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test AdresDAO -------------");

        //Maak een voorbeeldreiziger om ons voorbeeldadres aan te koppelen
        Reiziger exampleReiziger = new Reiziger(77, "A", "", "Broers", Date.valueOf("1971-12-03"));
        rdao.save(exampleReiziger);

        //Haal alle adressen uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        //Maak een nieuw adres en sla deze op in de database
        Adres adres = new Adres(77, "1234AB", "1", "Straatlaan", "Urk",77);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(adres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen");
        exampleReiziger.setAdres(adres);

        //Vind het zojuist aangemaakte adres via de id
        int id = 77;
        Adres foundById = adao.findById(id);
        System.out.println(String.format("[TEST] Gevonden door het zoeken naar id %s: %s", id, foundById));

        //Update het zojuist toegevoegde adres
        System.out.println("[TEST] Eerst: " + foundById.toString());
        Adres exampleAdres = new Adres(77, "1234BC", "2", "Laanstraat", "Nieuw-Urk", 77);
        adao.update(exampleAdres);
        Adres updatedAdres = adao.findById(id);
        System.out.println("na ReizigerDAO.update(): " + updatedAdres.toString());
        exampleReiziger.setAdres(updatedAdres);

        //Vind het nieuwe adres dmv de reiziger
        System.out.println(String.format("[TEST] We zoeken naar het adres van reiziger %s en vinden:", exampleReiziger));
        Adres foundByReiziger = adao.findByReiziger(exampleReiziger);
        System.out.println(foundByReiziger.toString());

        // Verwijder het nieuwe adres
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(exampleAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        //Verwijder ten slotte ook de voorbeeldreiziger
        rdao.delete(exampleReiziger);

    }
}