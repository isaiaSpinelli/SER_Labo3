import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;


public class KMLWriterJDOM {

    private Element root1;
    private Element root;
    private Document doc;
    final private String COLORLINE = "ffffffff";
    final private String FILLPLOYGONE = "0";

    public KMLWriterJDOM() {
        try {
            root1 = new Element("kml");
            // xmlns ne fonctionne pas
            root1.setAttribute("xmlns2","http://www.opengis.net/kml/2.2");

            root =  new Element("Document");
            root.setAttribute("id","root_doc");
            root1.addContent(root);
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
        Element placemark =  new Element("Placemark");
        root.addContent(placemark);
        // Ajoute le nom du pays
        Element name =  new Element("name");
        name.addContent(country.getName());
        placemark.addContent(name);

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
        Element style = new Element("Style");

        // Ajout le Style de la ligne
        Element lineStyle = new Element("LineStyle");
        Element color = new Element("Color");
        color.addContent(COLORLINE);
        lineStyle.addContent(color);
        // l'ajoute dans style
        style.addContent(lineStyle);

        // Ajout le Style du polygone
        Element polyStyle = new Element("PolyStyle");
        Element fill = new Element("fill");
        fill.addContent(FILLPLOYGONE);
        polyStyle.addContent(fill);
        // l'ajoute dans style
        style.addContent(polyStyle);

        // Ajoute le toute dans le placemark
        placemark.addContent(style);
    }

    /*
     * Permet d'ajouter les simples datas du pays
     */
    public void addExtendedData(Element placemark, Country country) {
        Element extendedData = new Element("ExtendedData");

        // Ajout le premier SimpleDate (ADMIN)
        Element simpleData = new Element("SimpleData");
        simpleData.setAttribute("name", "ADMIN");
        simpleData.addContent(country.getName());

        extendedData.addContent(simpleData);

        // Ajout le premier SimpleDate (ISO_A3)
        Element simpleData2 = new Element("SimpleData");
        simpleData2.setAttribute("name", "ISO_A3");
        simpleData2.addContent(country.getCode());

        extendedData.addContent(simpleData2);

        // Ajoute le toute dans le placemark
        placemark.addContent(extendedData);


    }

    public void addCoordinate(Element placemark, List<CustomPair> coordinates) {

        Element polygon = new Element("Polygon");

        // Ajout le outerBoundaryIs
        Element outerBoundaryIs = new Element("outerBoundaryIs");

        // Ajout le outerBoundaryIs
        Element linearRing = new Element("LinearRing");
        outerBoundaryIs.addContent(linearRing);

        // Ajout le outerBoundaryIs
        Element coord = new Element("coordinates");
        for (CustomPair customPair : coordinates){
            coord.addContent(customPair.toString() + " ");
        }
        linearRing.addContent(coord);

        polygon.addContent(outerBoundaryIs);

        // Ajoute le toute dans le placemark
        placemark.addContent(polygon);

    }

    public  boolean writeFile(String name) {
        try {
            doc = new Document(root1);
            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(doc, new FileWriter(name));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
