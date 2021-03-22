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


    /**
     * BFS
     * calcule l’itinéraire entre deux pays passant par le moins de frontières possibles.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     * @throws PaysNotFound exception en cas de pays inexistant.
     */
    public void calculerItineraireMinimisantNombreDeFrontieresOuss(String depart, String arrivee, String sortieXML) throws PaysNotFound {

        Deque<String> file = new ArrayDeque<>();
        Set<String> paysRencontres = new HashSet<>();
        Map<String, String> successeurs = new HashMap<>();

        String paysCourant = depart;

        do {
            for (String c :
                    arcsSortants(paysCourant)) {
                if (!paysRencontres.contains(c)) {
                    file.add(c);
                    paysRencontres.add(c);
                    successeurs.put(c, paysCourant);
                }
            }
            paysCourant = file.pop();
        } while (!file.isEmpty());


        List<String> response = toList(depart, arrivee, successeurs);
        exportXML(depart, arrivee, sortieXML, response);

    }


    /**
     * calculer un itinéraire en minimisant d’abord le nombre de pays traversés et ensuite la somme des populations.
     *
     * @param depart    pays de depart.
     * @param arrivee   pays d'arrivee.
     * @param sortieXML nom de la sortie XML.
     * @throws PaysNotFound exception en cas de pays inexistant.
     */
    public void dijkstraAmeliore(String depart, String arrivee, String sortieXML) throws PaysNotFound {
        if (!correspondanceCca3Countries.containsKey(depart) || !correspondanceCca3Countries.containsKey(arrivee))
            throw new IllegalArgumentException();
        

    }


    /**
     * Dijkstra
     * calcule l’itinéraire entre deux pays pour lequel la somme des populations des pays traversés est la plus petite.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de la sortie XML.
     * @throws PaysNotFound exception en cas de pays inexistant.
     */
    public void calculerItineraireMinimisantPopulationTotale(String depart, String arrivee, String sortieXML) throws PaysNotFound {

        if (!correspondanceCca3Countries.containsKey(depart) || !correspondanceCca3Countries.containsKey(arrivee))
            throw new PaysNotFound();

        Map<String, Long> poids = new HashMap<>();
        Map<String, String> successeurs = new HashMap<>();

        List<String> open = new ArrayList<>();
        List<String> closed = new ArrayList<>();

        for (Map.Entry<String, Country> entry :
                correspondanceCca3Countries.entrySet()) {
            poids.put(entry.getKey(), Long.MAX_VALUE);
            successeurs.put(entry.getKey(), null);
        }

        poids.put(depart, 0L);
        open.add(depart);

        while (!open.isEmpty()) {

            String min = trouverMinPays(open, poids);
            closed.add(min);
            open.remove(min);

            for (String p :
                    arcsSortants(min)) {
                if (!closed.contains(p)) {

                    long alt = poids.get(min) + correspondanceCca3Countries.get(p).getPopulation();

                    if (alt < poids.get(p)) {
                        poids.put(p, alt);
                        open.add(p);
                        successeurs.put(p, min);
                    }
                }
            }
        }

        List<String> resultat = toList(depart, arrivee, successeurs);
        exportXML(depart, arrivee, sortieXML, resultat);

    }


    /**
     * Renvoie le pays avec le plus petit poids.
     *
     * @param open  noeuds non-traites.
     * @param poids poids actuel de chaque pays.
     * @return le pays ayant le plus petit poids.
     */
    private String trouverMinPays(List<String> open, Map<String, Long> poids) {
        Long minPoids = Long.MAX_VALUE;
        String minPays = null;

        for (String pays :
                open) {
            if (poids.get(pays) < minPoids) {
                minPays = pays;
                minPoids = poids.get(pays);
            }
        }
        if (minPays == null) {
            System.out.println("Pas de min");
            minPays = open.get(0);
        }
        return minPays;
    }


    /**
     * Parcours une suite de successeurs et renvoie une liste.
     *
     * @param depart      pays départ.
     * @param arrivee     pays arrivee.
     * @param successeurs map successeurs.
     * @return liste contenant les pays.
     */
    private List<String> toList(String depart, String arrivee, Map<String, String> successeurs) {
        List<String> resultat = new ArrayList<>();
        String precedent = arrivee;

        resultat.add(precedent);
        while (!precedent.equals(depart)) {
            String newPrecedent = successeurs.get(precedent);
            resultat.add(newPrecedent);
            precedent = newPrecedent;
        }
        return resultat;
    }


    /**
     * Export le resultat en un fichier XML.
     *
     * @param depart    pays de départ.
     * @param arrivee   pays d'arrivée.
     * @param sortieXML nom de l'output XML.
     * @param resultat  itineraire contenant les pays.
     */
    private void exportXML(String depart, String arrivee, String sortieXML, List<String> resultat) {

        Collections.reverse(resultat);

        System.out.println("Export en cours ...");
        System.out.println("-> Trajet en " + resultat.size() + " pays trouvé");

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


    /**
     * ajoute un element au noeud parent.
     *
     * @param parent     noeud parent.
     * @param cca3       cca3 du pays.
     * @param nom        nom du pays.
     * @param population nombre de population du pays.
     */
    private void ajoutNoeud(Node parent, String cca3, String nom, String population) {
        Element element = parent.getOwnerDocument().createElement("pays");
        element.setAttribute("cca3", cca3);
        element.setAttribute("nom", nom);
        element.setAttribute("population", population);
        parent.appendChild(element);
    }
}
