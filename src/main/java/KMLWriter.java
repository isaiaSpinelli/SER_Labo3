
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;


public class KMLWriter {

    private Document doc;
    private Element root;
    final private String COLORLINE = "ffffffff";
    final private String FILLPLOYGONE = "0";

    public  KMLWriter() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Element kml = doc.createElementNS("http://www.opengis.net/kml/2.2", "kml");
            doc.appendChild(kml);
            root = doc.createElement("Document");
            root.setAttribute("id","root_doc");
            kml.appendChild(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * Ajout de plusieurs pays dans le fichier KML
     */
    public void addCountries(List<Country> countries) {

        // Pour chaque pays, l'ajoute
        for (Country country : countries){
            addCountry(country);
        }

    }

    /*
    * Ajout d'un pays dans le fichier KML
     */
    public void addCountry(Country country) {
        // Crée un nouveau Placemark
        Element placemark = doc.createElement("Placemark");
        root.appendChild(placemark);
        // Ajoute le nom du pays
        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(country.getName()));
        placemark.appendChild(name);

        // Ajoute le style de la forme
        addStyle(placemark);

        addExtendedData(placemark, country);

        // Pour chaque liste de coordinates
        for (List<CustomPair> listCoor : country.getCoordinates()){
            addCoordinate(placemark,listCoor);
        }

    }

    /*
    * Permet d'ajouter le style des polygones pour chaque pays
     */
    public void addStyle(Element placemark) {
        Element style = doc.createElement("Style");

        // Ajout le Style de la ligne
        Element lineStyle = doc.createElement("LineStyle");
        Element color = doc.createElement("Color");
        color.appendChild(doc.createTextNode(COLORLINE));
        lineStyle.appendChild(color);
        // l'ajoute dans style
        style.appendChild(lineStyle);

        // Ajout le Style du polygone
        Element polyStyle = doc.createElement("PolyStyle");
        Element fill = doc.createElement("fill");
        fill.appendChild(doc.createTextNode(FILLPLOYGONE));
        polyStyle.appendChild(fill);
        // l'ajoute dans style
        style.appendChild(polyStyle);

        // Ajoute le toute dans le placemark
        placemark.appendChild(style);
    }

    /*
     * Permet d'ajouter les simples datas du pays
     */
    public void addExtendedData(Element placemark, Country country) {
        Element extendedData = doc.createElement("ExtendedData");

        // Ajout le premier SimpleDate (ADMIN)
        Element simpleData = doc.createElement("SimpleData");
        simpleData.setAttribute("name", "ADMIN");
        simpleData.appendChild(doc.createTextNode(country.getName()));

        extendedData.appendChild(simpleData);

        // Ajout le premier SimpleDate (ISO_A3)
        Element simpleData2 = doc.createElement("SimpleData");
        simpleData2.setAttribute("name", "ISO_A3");
        simpleData2.appendChild(doc.createTextNode(country.getCode()));

        extendedData.appendChild(simpleData2);

        // Ajoute le toute dans le placemark
        placemark.appendChild(extendedData);


    }

    public void addCoordinate(Element placemark, List<CustomPair> coordinates) {

        Element polygon = doc.createElement("Polygon");

        // Ajout le outerBoundaryIs
        Element outerBoundaryIs = doc.createElement("outerBoundaryIs");

        // Ajout le outerBoundaryIs
        Element linearRing = doc.createElement("LinearRing");
        outerBoundaryIs.appendChild(linearRing);

        // Ajout le outerBoundaryIs
        Element coord = doc.createElement("coordinates");
        for (CustomPair customPair : coordinates){
            coord.appendChild(doc.createTextNode(customPair.toString() + " "));
        }
        linearRing.appendChild(coord);

        polygon.appendChild(outerBoundaryIs);

        // Ajoute le toute dans le placemark
        placemark.appendChild(polygon);

    }

    public  boolean writeFile(File file) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource src = new DOMSource(doc);
            StreamResult out = new StreamResult(file);
            transformer.transform(src, out);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
