public class Country {

    private String cca3, nom;
    int population;
    
    public Country(String cca3, String nom, int population){
        this.cca3 = cca3;
        this.nom = nom;
        this.population = population;
    }

    public String getCca3() {
        return this.cca3;
    }

    public String getNom() {
        return this.nom;
    }

    public int getPopulation() {
        return this.population;
    }
}
