import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

    private Country country;
    private Graph graph;

    private boolean isBorder;

    public SAXHandler(){
        super();
        this.country = null;
        this.graph = new Graph();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("country")){
            this.country = new Country(attributes.getValue("cca3"), attributes.getValue("name"), Integer.parseInt(attributes.getValue("population")));
            graph.ajouterSommet(this.country);
        }else if (qName.equals("border")){
            this.isBorder = true;
        }


    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(qName.equals("border")) {
            this.isBorder = false;
        }else if (qName.equals("country")){
            this.country = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (this.isBorder)
            this.graph.ajouterArc(new Route(this.country.getCca3(), new String(ch, start, length)));
    }

    /**
     * renvoie le graph créé sur base de l'input XML.
     * @return le graph construit.
     */
    public Graph getGraph() {
        return this.graph;
    }
}
