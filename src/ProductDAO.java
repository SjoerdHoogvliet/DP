import java.util.List;

public interface ProductDAO {
    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);
    public List<Product> findByOVChipkaart(OVChipkaart ovck);
    public List<Product> findAll();
    public void setOvckdao(OVChipkaartDAO ovckdao);
}
