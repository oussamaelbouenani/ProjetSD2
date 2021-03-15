import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    private Map<String, Country> correspondanceCca3Countries;
    private Map<Country,Set<Route>> listeDAdjacence;

    //TODO Disjkstra : chaque sommet se souvient du sommet précedent --> donc utiliser une map

    public Graph(){
		correspondanceCca3Countries = new HashMap<String, Country>();
		listeDAdjacence = new HashMap<Country,Set<Route>>();
	}
    

    protected void ajouterSommet(Country country) {
		this.correspondanceCca3Countries.put(country.getCca3(), country);
    	this.listeDAdjacence.put(country, new HashSet<>());
    }

    protected void ajouterArc(Route f) {
    	this.listeDAdjacence.get(this.correspondanceCca3Countries.get(f.getSource())).add(f);
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
     * @param depart pays de départ.
     * @param arrivee pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieres(String depart, String arrivee, String sortieXML) {
        ArrayDeque<List<Country>> routes = new ArrayDeque<List<Country>>();
        List<Country> response = new ArrayList<Country>();
        boolean firstBoucle = true;
        Country cDepart;
        
    	
    	while(response.isEmpty()) {

			List<Country> routesTmp = new ArrayList<>();
    		if(firstBoucle) {
    			cDepart = this.correspondanceCca3Countries.get(depart);
    		}else {
				 routesTmp = routes.pop();
    			cDepart = routesTmp.get(routesTmp.size()-1);
    		}
    			
    		Set<Route> routesSortant = arcsSortants(cDepart);
    		
        	for(Route r:routesSortant) {
        		List<Country> itinTmp;

				itinTmp = routesTmp;
        		itinTmp.add(this.correspondanceCca3Countries.get(r.getDestination()));
        		
    			if(r.getDestination().equals(arrivee)) {
    				response = itinTmp;
    			}else {
    				routes.add(itinTmp);
    			}
        	}
        	firstBoucle = false;
    	}

    	exportXML(response, depart, arrivee, sortieXML);

    }

    /**
     * calcule l’itinéraire entre deux pays pour lequel la somme des populations des pays traversés est la plus petite.
     * @param depart pays de départ.
     * @param arrivee pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantPopulationTotale(String depart, String arrivee, String sortieXML) {
        //TODO
    }

    private void exportXML(List<Country> resultat, String depart, String arrivee, String sortieXML){

		System.out.println("Export en cours ...");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.newDocument();

			Element racine = document.createElement("itineraire");
			racine.setAttribute("arrivee", arrivee);
			racine.setAttribute("depart", depart);

			int somme = 0;
			for (Country country:
				 resultat) {
				ajoutNoeud(racine, country.getCca3(), country.getNom(), String.valueOf(country.getPopulation()));
				somme += country.getPopulation();
			}

			racine.setAttribute("sommePopulation", String.valueOf(somme));

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.transform(new SAXSource((InputSource) document),new StreamResult(new File(sortieXML)));

			System.out.println("Export fini");


		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}


	}

	private void ajoutNoeud(Node parent, String cca3, String nom, String population)
	{
		System.out.println("Ajout noeud");

		Element element = parent.getOwnerDocument().createElement("pays");
		element.setAttribute("cca3", cca3);
		element.setAttribute("nom", nom);
		element.setAttribute("population", population);
		parent.appendChild(element);
	}
}
