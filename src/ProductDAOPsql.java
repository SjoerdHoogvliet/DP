import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{
    OVChipkaartDAO ovckdao;
    @Override
    public boolean save(Product product){
        try{
            String productQuery = "INSERT INTO product(product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?);";

            PreparedStatement pstProduct = Main.connection.prepareStatement(productQuery);
            pstProduct.setInt(1, product.getProductNummer());
            pstProduct.setString(2, product.getNaam());
            pstProduct.setString(3, product.getBeschrijving());
            pstProduct.setDouble(4, product.getPrijs());
            pstProduct.execute();
            pstProduct.close();

            if(this.ovckdao != null && product.getChipkaartenMetProduct() != null){
                for(OVChipkaart ovck : product.getChipkaartenMetProduct()){
                    String OVckPQuery = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer, last_update) VALUES (?, ?, ?);";

                    PreparedStatement pstOVckP = Main.connection.prepareStatement(OVckPQuery);
                    pstOVckP.setInt(1, ovck.getKaartNummer());
                    pstOVckP.setInt(2, product.getProductNummer());
                    pstOVckP.setDate(3, Date.valueOf(LocalDate.now()));
                    pstOVckP.execute();
                    pstOVckP.close();
                }
            }

            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at save in ProductDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try{
            String productQuery = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?;";

            PreparedStatement pst = Main.connection.prepareStatement(productQuery);
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getProductNummer());
            pst.execute();
            pst.close();

            if(this.ovckdao != null && product.getChipkaartenMetProduct() != null){
                for(OVChipkaart ovck : product.getChipkaartenMetProduct()){
                    //Place query for updating all entries with this product in ovchipkaart_product
                    if(ovckdao.findAll().contains(ovck)){
                        String OVckPQuery = "UPDATE ov_chipkaart_product SET last_updated = ? WHERE kaart_nummer = ? AND product_nummer = ? ";

                        PreparedStatement pstOVckP = Main.connection.prepareStatement(OVckPQuery);
                        pstOVckP.setDate(1, Date.valueOf(LocalDate.now()));
                        pstOVckP.setInt(2, ovck.getKaartNummer());
                        pstOVckP.setInt(3, product.getProductNummer());
                        pstOVckP.execute();
                        pstOVckP.close();
                    }
                }
            }

            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at update in ProductDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try{
            if(this.ovckdao != null && product.getChipkaartenMetProduct() != null){
                for(OVChipkaart ovck : product.getChipkaartenMetProduct()){

                    String OVckPQuery = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ? AND product_nummer = ?";

                    PreparedStatement pstOVckP = Main.connection.prepareStatement(OVckPQuery);
                    pstOVckP.setInt(1, ovck.getKaartNummer());
                    pstOVckP.setInt(2, product.getProductNummer());
                    pstOVckP.execute();
                    pstOVckP.close();

                }
            }

            String productQuery = "DELETE FROM product WHERE product_nummer = ?;";

            PreparedStatement pstProduct = Main.connection.prepareStatement(productQuery);
            pstProduct.setInt(1, product.getProductNummer());
            pstProduct.execute();
            pstProduct.close();

            return true;
        }catch(SQLException sqle){
            System.err.println("[SQLException] Something went wrong with the SQL at delete in ProductDAOPsql: " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovck){
        try{
            String query =  "SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs " +
                                "FROM product AS p " +
                                "INNER JOIN ov_chipkaart_product ovckp ON p.product_nummer = ovckp.product_nummer " +
                                "INNER JOIN ov_chipkaart ck ON ck.kaart_nummer = ovckp.kaart_nummer " +
                                "WHERE ck.kaart_nummer = ?;";

            PreparedStatement pst = Main.connection.prepareStatement(query);
            pst.setInt(1, ovck.getKaartNummer());
            ResultSet rs = pst.executeQuery();

            List<Product> producten = new ArrayList<>();
            while(rs.next()){
                //System.out.println("Iteratie over de resultaten van de findByOVChipkaart query");
                int productNummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                Product p = new Product(productNummer, naam, beschrijving, prijs);
                producten.add(p);
            }

            pst.close();
            rs.close();
            return producten;
        }
        catch(SQLException sqle){
            System.err.println("[SQLException]Something went wrong with findByOVChipkaart in ProductDAOPsql: " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public List<Product> findAll(){
        try{
            String query = "SELECT * FROM product";

            PreparedStatement pst = Main.connection.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            List<Product> producten = new ArrayList<>();

            while(rs.next()){
                int productNummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                Product p = new Product(productNummer, naam, beschrijving, prijs);
                producten.add(p);
            }

            pst.close();
            rs.close();
            return producten;
        }
        catch (SQLException sqle){
            System.err.println("[SQLException]Something went wrong with findAll in ProductDAOPsql: " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public void setOvckdao(OVChipkaartDAO ovckdao){
        this.ovckdao = ovckdao;
    }
}
