import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DOMMain {

	public static void main(String[] args) {
		try {
            File inputFile = new File("countries.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            
            Graph g = new Graph(doc);
            g.calculerItineraireMinimisantNombreDeFrontieresOuss("BEL", "IND", "output.xml");
            g.calculerItineraireMinimisantPopulationTotale("BEL", "IND", "output2.xml");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
	}

}
