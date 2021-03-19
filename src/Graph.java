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
    private Map<String, Set<String>> listeDAdjacence;

    //TODO Disjkstra : chaque sommet se souvient du sommet précedent --> donc utiliser une map

    public Graph() {
        this.correspondanceCca3Countries = new HashMap<>();
        this.listeDAdjacence = new HashMap<>();
    }


    protected void ajouterSommet(Country country) {
        this.correspondanceCca3Countries.put(country.getCca3(), country);
        this.listeDAdjacence.put(country.getCca3(), new HashSet<>());
    }

    protected void ajouterArc(String depart, String destination) throws PaysNotFound {
        this.listeDAdjacence.get(depart).add(destination);
    }

    public Set<String> arcsSortants(String country) throws PaysNotFound {
        if (!correspondanceCca3Countries.containsKey(country))
            throw new PaysNotFound(country);

        return this.listeDAdjacence.get(country);
    }

    public boolean sontAdjacents(String a1, String a2) throws PaysNotFound {
        if (!correspondanceCca3Countries.containsKey(a1) || !correspondanceCca3Countries.containsKey(a2)) {
            throw new PaysNotFound(a1 + " " + a2);
        }
        return listeDAdjacence.get(a1).contains(a2);
    }


    /**
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    /**
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

     }**/

    /**
     * BFS
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     */
    public void calculerItineraireMinimisantNombreDeFrontieresOuss(String depart, String arrivee, String sortieXML) throws PaysNotFound {

        Deque<String> file = new ArrayDeque<>();
        Deque<String> response = new ArrayDeque<>();

        Set<String> paysRencontres = new HashSet<>();

        Map<String, String> successeurs = new HashMap<>();

        boolean trouve = false;

        String paysCourant = depart;

        do {
            for (String c :
                    arcsSortants(paysCourant)) {

                if (c.equals(arrivee))
                    trouve = true;
                if (!paysRencontres.contains(c)) {
                    file.add(c);
                    paysRencontres.add(c);
                    successeurs.put(c, paysCourant);
                }
            }
            paysCourant = file.pop();
        } while (!trouve);

        String precedent = arrivee;
        response.add(precedent);
        while (!precedent.equals(depart)) {
            String newPrecedent = successeurs.get(precedent);
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
    public void calculerItineraireMinimisantPopulationTotale(String depart, String arrivee, String sortieXML) throws PaysNotFound {

        String sommetCourrant = depart;

        Set<String> paysRencontres = new HashSet<>();
        Set<String> etiquetteProvisoire = new HashSet<>();
        Deque<String> etiquetteDefinitives = new ArrayDeque<>();

        etiquetteDefinitives.add(sommetCourrant);

        long lesMin = 0;

        while (!sommetCourrant.equals(arrivee)) {


            paysRencontres.add(sommetCourrant);

            // étiquette provisoire -> pays limitrophes NON RENCONTRES.
            for (String c :
                    arcsSortants(sommetCourrant)) {
                if (!paysRencontres.contains(c)) {
                    etiquetteProvisoire.add(c);
                }
            }

            // trouver min
            int min = Integer.MAX_VALUE;

            String paysMin = "";
            for (String c :
                    etiquetteProvisoire) {

                Country country = correspondanceCca3Countries.get(c);
                if (min > country.getPopulation() + lesMin) {
                    min = country.getPopulation();
                    paysMin = c;
                }
            }


            // Nouveau sommet
            sommetCourrant = paysMin;
            System.out.println("--> " + paysMin);
            lesMin += correspondanceCca3Countries.get(sommetCourrant).getPopulation();
            etiquetteDefinitives.addLast(sommetCourrant);

        }

        exportXML(etiquetteDefinitives, depart, arrivee, sortieXML);

    }

    private void exportXML(Deque<String> resultat, String depart, String arrivee, String sortieXML) {

        System.out.println("Export en cours ...");

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
            for (String cca3 :
                    resultat) {
                Country country = correspondanceCca3Countries.get(cca3);
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
