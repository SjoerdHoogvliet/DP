import java.sql.*;
import java.util.List;

public class Main {
    static Connection connection;
    public static void main(String[] args){
        try{
            ReizigerDAO rdao = new ReizigerDAOPsql();
            AdresDAO adao = new AdresDAOPsql();
            OVChipkaartDAO ovckdao = new OVChipkaartDAOPsql();
            ProductDAO pdao = new ProductDAOPsql();
            rdao.setAdao(adao);
            rdao.setOvckdao(ovckdao);
            pdao.setOvckdao(ovckdao);

            Main.getConnection();
//            Main.testReizigerDAO(rdao);
//            Main.testAdresDao(adao, rdao);
//            Main.testOVChipkaartDAO(ovckdao, rdao);
            Main.testProductDAO(pdao, ovckdao, rdao);
            Main.closeConnection();
        }catch(SQLException sqle){
            System.err.println("[SQLException] There was an exception in one of the test blocks: " + sqle.getMessage());
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

        //Vind het zojuist aangemaakte adres via de id
        int id = 77;
        Adres foundById = adao.findById(id);
        System.out.println(String.format("[TEST] Gevonden door het zoeken naar id %s: %s", id, foundById));

        //Update het zojuist toegevoegde adres
        System.out.println("[TEST] Eerst: " + foundById.toString());
        Adres exampleAdres = new Adres(77, "1234BC", "2", "Laanstraat", "Nieuw-Urk", 77);
        adao.update(exampleAdres);
        Adres updatedAdres = adao.findById(id);
        System.out.println("na AdresDAO.update(): " + updatedAdres.toString());

        //Vind het nieuwe adres dmv de reiziger
        System.out.println(String.format("[TEST] We zoeken naar het adres van reiziger %s en vinden:", rdao.findById(exampleReiziger.getId())));
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

    private static void testOVChipkaartDAO(OVChipkaartDAO ovckdao, ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        //Maak een voorbeeldreiziger om onze kaarten aan te koppelen
        Reiziger exampleReiziger = new Reiziger(77, "A", "", "Broers", Date.valueOf("1971-12-03"));
        rdao.save(exampleReiziger);

        //Haal alle chipkaarten uit de database
        System.out.println("[TEST] Alle OV Chipkaarten:");
        for(OVChipkaart k : ovckdao.findAll()){
            System.out.println(k.toString());
        }

        //Maak een nieuwe OV Chipkaart aan en sla deze op
        OVChipkaart kaart1 = new OVChipkaart(12345, Date.valueOf("2024-01-01"), 2, 20.50, 77);
        System.out.print("[Test] Eerst " + ovckdao.findAll().size() + " OV Chipkaarten, na OVChipkaartDAO.save() ");
        ovckdao.save(kaart1);
        System.out.println(ovckdao.findAll().size() + " OV Chipkaarten");

        //Update de nieuwe OV Chipkaart
        OVChipkaart kaart1Nieuw = new OVChipkaart(12345, Date.valueOf("2026-01-01"), 2, 24.50, 77);
        System.out.println("[TEST] Eerst " + ovckdao.findByReiziger(exampleReiziger).toString());
        ovckdao.update(kaart1Nieuw);
        System.out.println("daarna: " + ovckdao.findByReiziger(exampleReiziger).toString());

        //Vind de zojuist aangemaakte kaart op basis van de reiziger (met 1 kaart)
        System.out.println("[TEST] We zoeken naar de chipkaarten van reiziger met id " + exampleReiziger.getId());
        for(OVChipkaart k : ovckdao.findByReiziger(exampleReiziger)){
            System.out.println(k.toString());
        }

        //Vind de zojuist aangemaakte kaarten op basis van de reiziger (met meerdere kaarten)
        OVChipkaart kaart2 = new OVChipkaart(23456, Date.valueOf("2025-01-01"), 1, 50.25, 77);
        ovckdao.save(kaart2);
        System.out.println("[TEST] We zoeken naar de chipkaarten van reiziger met id " + exampleReiziger.getId());
        for(OVChipkaart k : ovckdao.findByReiziger(exampleReiziger)){
            System.out.println(k.toString());
        }

        //Verwijder de nieuwe kaarten
        ovckdao.delete(kaart1Nieuw);
        ovckdao.delete(kaart2);

        //Verwijder tenslotte de aangemaakte reiziger
        rdao.delete(exampleReiziger);
    }

    private static void testProductDAO(ProductDAO pdao, OVChipkaartDAO ovckdao,ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test ProductDAO -------------");

        //Maak een testreiziger aan, hieraan kunnen we de OV-chipkaart koppelen waaraan de producten gekoppeld worden
        Reiziger exampleReiziger = new Reiziger(77, "A", "", "Broers", Date.valueOf("1971-12-03"));
        rdao.save(exampleReiziger);

        //Maak een test OV-Chipkaart
        OVChipkaart kaart1 = new OVChipkaart(12345, Date.valueOf("2024-01-01"), 2, 20.50, 77);
        kaart1.setReiziger(exampleReiziger);
        ovckdao.save(kaart1);

        //Maak een nieuw product, zet dit op OV-Chipkaart 12345 en sla dit op
        Product testP1 = new Product(56789, "Reisproduct", "Product voor reizen", 12.50);
        kaart1.addProduct(testP1);
        System.out.print("[TEST]Eerst " + pdao.findAll().size() + " producten, ");
        pdao.save(testP1);
        System.out.println("na pdao.save() " + pdao.findAll().size() + " producten.");

        //Zoek nu ons product op basis van de OV-Chipkaart
        System.out.println("[TEST]Zoeken op OV-Chipkaart " + kaart1 + " geeft:");
        System.out.println(pdao.findByOVChipkaart(kaart1));

        //Test de update van product
        testP1.setBeschrijving("Voor reizen bedoeld product");
        System.out.print("[TEST]Update product: " + pdao.findByOVChipkaart(kaart1));
        pdao.update(testP1);
        System.out.println(" na update: " + pdao.findByOVChipkaart(kaart1));

        //Delete nu het product
        System.out.print("[TEST]Eerst " + pdao.findAll().size() + " producten, ");
        pdao.delete(testP1);
        System.out.println("na pdao.delete() " + pdao.findAll().size() + " producten.");

        //Verwijder tenslotte de test OV-Chipkaart en reiziger
        ovckdao.delete(kaart1);
        rdao.delete(exampleReiziger);
    }
}