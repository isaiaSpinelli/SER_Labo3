import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.util.List;

/**
 * Cette classe permet de généré un fichier kml suite à la lecture et parsage du fichier geojson
 */
public class KMLWriterJDOM {

    private Element kmlRoot;
    private Element document;
    private Document documentType;

    final private String COLORLINE = "ffffffff";
    final private String FILLPLOYGONE = "0";

    /**
     * Permet d'instancier la structure principal du fichier kml, l'en-tête root et document
     */
    public KMLWriterJDOM() {
        try {
            kmlRoot = new Element("kml");
            kmlRoot.setAttribute("xmlns2","http://www.opengis.net/kml/2.2");
            document =  new Element("Document");
            document.setAttribute("id","root_doc");
            kmlRoot.addContent(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Ajoute tous les pays un à un dans le fichier depuis la liste fournie
     * @param countries liste de pays à ajouter au fichier
     */
    public void addCountries(List<Country> countries) {
        // Les pays sont ajoutés un à un
        for (Country country : countries){
            addCountry(country);
        }
    }

    /**
     * Ajoute le pays dans le fichier
     * @param country le pays à ajouter
     */
    private void addCountry(Country country) {
        // Créé l'instance englobant le pays et ses propriétés
        Element placemark =  new Element("Placemark");
        document.addContent(placemark);
        // Ajoute le nom du pays
        Element name =  new Element("name");
        name.addContent(country.getName());
        placemark.addContent(name);

        // Ajoute le style permettant d'entourer en blanc la frontière du pays
        addStyle(placemark);

        // Les coordonnées sont entièrements ajoutées
        addCoordinate(placemark, country.getCoordinates());
    }

    /**
     * Ajoute un style, un contour blanc à la frontière du pays
     * @param placemark
     */
    private void addStyle(Element placemark) {
        Element style = new Element("Style");

        // Ajoute le Style du trait
        Element lineStyle = new Element("LineStyle");
        Element color = new Element("Color");
        color.addContent(COLORLINE);
        lineStyle.addContent(color);
        // Ajoute dans l'objet style
        style.addContent(lineStyle);

        // Ajoute le style du polygone
        Element polyStyle = new Element("PolyStyle");
        Element fill = new Element("fill");
        fill.addContent(FILLPLOYGONE);
        polyStyle.addContent(fill);
        // Ajoute dans l'objet style
        style.addContent(polyStyle);

        // Le style est ajouté dans le placemark du pays
        placemark.addContent(style);
    }

    /**
     * Ajoute les coordonnées en gérant les polygons simples ou multipolygons
     * @param placemark représente l'englobeur d'un pays dans le fichier
     * @param coordinates les coordonnées des zones du pays
     */
    private void addCoordinate(Element placemark, List<List<CustomPair>> coordinates) {
        boolean isMulti = coordinates.size() > 1;
        Element multiGeometry = null;

        // Vérifie qu'il y a plusieurs polygons, donc multipolygons
        if (isMulti){
            multiGeometry = new Element("MultiGeometry");
        }

        // On effectue l'ajout des coordonnées que si elles existent
        if(coordinates.size() > 0) {
            Element polygon = null;
            // Parcourt chaque polygons
            for (List<CustomPair> coordinate : coordinates) {
                polygon = new Element("Polygon");
                StringBuilder coordToBuild = new StringBuilder();

                // Parcourt la liste de coordonnées de chaque polygons
                for (int i = 0; i < coordinate.size(); ++i) {
                    // Chaque coordonnée est concaténée pour former une seule chaîne
                    coordToBuild.append(coordinate.get(i).toString()).append("\n\t\t\t\t\t\t");
                }
                // Implémente les balises complètes des coordonnées
                Element completeCoordinates = new Element("outerBoundaryIs").addContent(new Element("LinearRing")
                        .addContent(new Element("coordinates").setText(coordToBuild.toString())));
                polygon.addContent(completeCoordinates);

                // On ajoute le polygon au multipolygons s'il y en plusieurs
                if (isMulti) {
                    multiGeometry.addContent(polygon);
                }
            }
            // Les coordonnées finales sont ajoutés au placemark qui englobe le pays et ses propriétés
            placemark.addContent(isMulti ? multiGeometry : polygon);

        }
    }

    /**
     *  Écrit la sortie générée dans le fichier kml
     * @param name le nom du fichier à générer
     */
    public void writeFile(String name) {
        try {
            documentType = new Document(kmlRoot);
            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(documentType, new FileWriter(name));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
