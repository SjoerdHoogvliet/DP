import java.sql.Date;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getNaam(){
        if(tussenvoegsel == null){
            return voorletters + ". " + achternaam;
        }
        else{
            return voorletters + ". "  + tussenvoegsel + " " + achternaam;
        }
    }
    @Override
    public String toString(){
        if(tussenvoegsel == null){
         return String.format("Reiziger id: %d; %s. %s is geboren op: %s", id, voorletters,
                 achternaam, geboortedatum);
        }
        return String.format("Reiziger id: %d; %s. %s %s is geboren op: %s", id,
            voorletters, tussenvoegsel, achternaam, geboortedatum);
    }

}
