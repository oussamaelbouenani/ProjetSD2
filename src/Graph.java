import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    private Map<String, Country> correspondanceCca3Countries = new HashMap<String, Country>();
    private Map<Country,Set<Route>> listeDAdjacence = new HashMap<Country,Set<Route>>();
    

    protected void ajouterSommet(Country country) {
		this.correspondanceCca3Countries.put(country.getCca3(), country);
    	this.listeDAdjacence.put(country, new HashSet<>());
    }

    protected void ajouterArc(Route f) {
    	this.listeDAdjacence.get(f.getSource()).add(f);
    }

    public Set<Route> arcsSortants(Country a){
    	return this.listeDAdjacence.get(a);
    }

    public boolean sontAdjacents(Country a1, Country a2) {
    	Set<Route> set1 = this.listeDAdjacence.get(a1);
		Set<Route> set2 = this.listeDAdjacence.get(a2);

		for (Route f1:
			 set1) {
			if (a2.equals(f1.getDestination())) return true;
		}
		for (Route f2:
			 set2) {
			if (a1.equals(f2.getDestination())) return true;
		}
		return false;
    }


    /**
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     * @param départ pays de départ.
     * @param arrivée pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieres(String depart, String arrivee, String sortieXML) {
        ArrayDeque<Route> itinRoutes = new ArrayDeque<Route>();
        ArrayDeque<Route> routes = new ArrayDeque<Route>();
        boolean find = false;
    	Country cDepart = this.correspondanceCca3Countries.get(depart);
    	Country cArrivee = this.correspondanceCca3Countries.get(arrivee);
    	
    	while(!find) {
    		Set<Route> tmp = arcsSortants(cDepart);
        	for(Route r:tmp) {
        		routes.push(r);
        	}
    	}
    }

    /**
     * calcule l’itinéraire entre deux pays pour lequel la somme des populations des pays traversés est la plus petite.
     * @param départ pays de départ.
     * @param arrivée pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantPopulationTotale(String depart, String arrivee, String sortieXML) {
        //TODO
    }

	public Map<String, Country> getCorrespondanceCca3Countries() {
		return correspondanceCca3Countries;
	}

	public Map<Country, Set<Route>> getListeDAdjacence() {
		return listeDAdjacence;
	}
}
