import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class Graph {

    private Map<String, Country> correspondanceCca3Countries;
    private Map<Country, Set<Route>> listeDAdjacence;

    //TODO Disjkstra : chaque sommet se souvient du sommet précedent --> donc utiliser une map

    public Graph() {
        this.correspondanceCca3Countries = new HashMap<>();
        this.listeDAdjacence = new HashMap<>();
    }


    protected void ajouterSommet(Country country) {
        this.correspondanceCca3Countries.put(country.getCca3(), country);
        this.listeDAdjacence.put(country, new HashSet<>());
    }

    protected void ajouterArc(Route f) {
        this.listeDAdjacence.get(this.correspondanceCca3Countries.get(f.getSource())).add(f);
    }

    public Set<Route> arcsSortants(Country a) {
        return this.listeDAdjacence.get(a);
    }

    public boolean sontAdjacents(Country a1, Country a2) {
        Set<Route> set1 = this.listeDAdjacence.get(a1);
        Set<Route> set2 = this.listeDAdjacence.get(a2);

        for (Route f1 :
                set1) {
            if (a2.equals(f1.getDestination())) return true;
        }
        for (Route f2 :
                set2) {
            if (a1.equals(f2.getDestination())) return true;
        }
        return false;
    }


    /**
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieres(String depart, String arrivee, String sortieXML) {
        ArrayDeque<List<Country>> routes = new ArrayDeque<>();
        List<Country> response = new ArrayList<>();
        boolean firstBoucle = true;
        Country cDepart;

        while (response.isEmpty()) {

            List<Country> routesTmp = new ArrayList<>();
            if (firstBoucle) {
                cDepart = this.correspondanceCca3Countries.get(depart);
            } else {
                routesTmp = routes.pop();
                cDepart = routesTmp.get(routesTmp.size() - 1);
            }

            Set<Route> routesSortant = arcsSortants(cDepart);

            for (Route r : routesSortant) {
                List<Country> itinTmp;

                itinTmp = routesTmp;
                itinTmp.add(this.correspondanceCca3Countries.get(r.getSource()));

                if (r.getDestination().equals(arrivee)) {
                    response = itinTmp;
                } else {
                    routes.add(itinTmp);
                }
            }
            firstBoucle = false;
        }

        //exportXML(response, depart, arrivee, sortieXML);

    }

    /**
     * BFS
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieresOuss(String depart, String arrivee, String sortieXML) {

        Country countryDepart = correspondanceCca3Countries.get(depart);
        Country countryArrivee = correspondanceCca3Countries.get(arrivee);

        Deque<Country> file = new ArrayDeque<>();
        Deque<Country> response = new ArrayDeque<>();

        Set<Country> paysRencontres = new HashSet<>();

        Map<Country, Country> successeurs = new HashMap<>();

        boolean trouve = false;

        Country paysCourant = countryDepart;

        do {
            for (Route r :
                    arcsSortants(paysCourant)) {
                Country c = correspondanceCca3Countries.get(r.getDestination());
                if (c.equals(countryArrivee))
                    trouve = true;
                if (!paysRencontres.contains(c)) {
                    file.add(c);
                    paysRencontres.add(c);
                    successeurs.put(c, paysCourant);
                }
            }
            paysCourant = file.pop();
        } while (!trouve);

        Country precedent = countryArrivee;
        response.add(precedent);
        while (!precedent.equals(countryDepart)) {
            Country newPrecedent = successeurs.get(precedent);
            response.addFirst(newPrecedent);
            precedent = newPrecedent;
        }

        exportXML(response, depart, arrivee, sortieXML);

    }


    /**
     * Dijkstra
     * calcule l’itinéraire entre deux pays pour lequel la somme des populations des pays traversés est la plus petite.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantPopulationTotale(String depart, String arrivee, String sortieXML) {

        Country cDepart = correspondanceCca3Countries.get(depart);
        Country cArrivee = correspondanceCca3Countries.get(arrivee);

        Set<Country> paysRencontres = new HashSet<>();
        Set<Country> paysNonRencontres = new HashSet<>();

        Map<Country, Country> successeurs = new HashMap<>();

        //TODO


    }

    private void exportXML(Deque<Country> resultat, String depart, String arrivee, String sortieXML) {

        System.out.println("Export en cours ...");

        resultat.forEach(x -> System.out.println(x.getNom()));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document document = db.newDocument();

            Element racine = document.createElement("itineraire");
            racine.setAttribute("arrivee", arrivee);
            racine.setAttribute("depart", depart);

            document.appendChild(racine);

            long somme = 0;
            for (Country country :
                    resultat) {
                ajoutNoeud(racine, country.getCca3(), country.getNom(), String.valueOf(country.getPopulation()));
                somme += country.getPopulation();
            }
            racine.setAttribute("nbPays", String.valueOf(resultat.size()));
            racine.setAttribute("sommePopulation", String.valueOf(somme));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(sortieXML));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

            System.out.println("Export fini !");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }


    }

    private void ajoutNoeud(Node parent, String cca3, String nom, String population) {
        Element element = parent.getOwnerDocument().createElement("pays");
        element.setAttribute("cca3", cca3);
        element.setAttribute("nom", nom);
        element.setAttribute("population", population);
        parent.appendChild(element);
    }
}
