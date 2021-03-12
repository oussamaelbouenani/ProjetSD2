public class Country {

    private String cca3, nom;
    
    public Country(String cca3, String nom){
        this.cca3 = cca3;
        this.nom = nom;
    }

    public String getCca3() {
        return this.cca3;
    }

    public String getNom() {
        return nom;
    }
}
