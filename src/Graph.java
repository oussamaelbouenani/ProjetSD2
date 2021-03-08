import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Graph {

    protected Map<String, Country> correspondanceCca3Countries ;

    public Graph()  {
        correspondanceCca3Countries= new HashMap<String, Country>();
    }

    public void constructFromXML (String xmlFile)throws Exception {
        File xml = new File(xmlFile);
        DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
        Document doc = docBuild.parse(xml);
        NodeList countries = doc.getElementsByTagName("countries");
        for (int i = 0; i < countries.getLength(); i++) {
            Node country = countries.item(i);
            Element elCountries = (Element) country;
            String cca3 = elCountries.getAttribute("cca3");
            String name = elCountries.getAttribute("name");
            Country a = new Country(cca3, name);
            correspondanceCca3Countries.put(cca3, a);
            ajouterSommet(a);
        }
        for (int i = 0; i < countries.getLength(); i++) {
            Node country = countries.item(i);
            Element elCountries = (Element) country;
            String cca3 = elCountries.getAttribute("cca3");
            NodeList routes = elCountries.getElementsByTagName("route");
            for (int j = 0; j < routes.getLength(); j++) {
                Node route = routes.item(j);
                Element elFlight = (Element) route;
                String dest = elFlight.getTextContent();
                Route f = new Route(correspondanceCca3Countries.get(cca3), correspondanceCca3Countries.get(dest));
                ajouterArc(f);
            }
        }
    }

    protected abstract void ajouterSommet(Country country);

    protected abstract void ajouterArc(Route f);

    public abstract Set<Route> arcsSortants(Country a);

    public abstract boolean sontAdjacents(Country a1, Country a2);


    /**
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     * @param départ pays de départ.
     * @param arrivée pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieres(String départ, String arrivée, String sortieXML) {
        //TODO

    }

    /**
     * calcule l’itinéraire entre deux pays pour lequel la somme des populations des pays traversés est la plus petite.
     * @param départ pays de départ.
     * @param arrivée pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantPopulationTotale(String départ, String arrivée, String sortieXML) {
        //TODO
    }
}
