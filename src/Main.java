import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            File inputFile = new File("countries.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler userhandler = new SAXHandler();
            saxParser.parse(inputFile, userhandler);
            Graph g = userhandler.getGraph();
            g.calculerItineraireMinimisantNombreDeFrontieresOuss("BEL", "IND", "output.xml");
            g.calculerItineraireMinimisantPopulationTotale("BEL", "IND", "output2.xml");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
