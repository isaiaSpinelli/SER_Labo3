import java.io.IOException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.output.*;

class Main {
    private static Boolean isMultiGeometry = true;

    private static Element createSimpleField(String name, String type){
        Element simpleField = new Element("SimpleField");
        simpleField.setAttribute(new Attribute("name", name));
        simpleField.setAttribute(new Attribute("type", type));
        return simpleField;
    }

    private static Element createSimpleData(String name, String text){
        Element simpleData = new Element("SimpleData");
        simpleData.setAttribute(new Attribute("name", name));
        simpleData.setText(text);
        return simpleData;
    }

    public static void main(String[] args) {

        GeojsonReader georeader = new GeojsonReader("countries.geojson");
        georeader.parse();
        List<Country> countryList = georeader.getCountryList();

        // Crée un KML et ajoutes les pays
        KMLWriterJDOM klm = new KMLWriterJDOM();
        klm.addCountries(countryList);
        // Ecrit le fichier
        klm.writeFile("src/test.KML");

/*
        // Crée un KML et ajoutes les pays
        KMLWriter klm = new KMLWriter();
        klm.addCountries(countryList);

        // Crée le fichier
        File file = new File("test.KML");
        // Ecrit le fichier
        klm.writeFile(file);*/


        /*
        Element root = new Element("kml");
        root.setAttribute(new Attribute("xmlns", "http://www.opengis.net/kml/2.2"));
        Document doc = new Document(root);

        Element document = new Element("Document");
        document.setAttribute(new Attribute("id", "root_doc"));

        Element schema = new Element("Schema");
        schema.setAttribute(new Attribute("name", "SELECT"));
        schema.setAttribute(new Attribute("id", "SELECT"));
        schema.addContent(createSimpleField("ADMIN", "string"));
        schema.addContent(createSimpleField("ISO_A3", "string"));
        document.addContent(schema);

        Element folder = new Element("Folder");
        folder.addContent(new Element("name").setText("SELECT"));

        int nbLieu = 1;
        for(int i = 0; i < nbLieu; ++i){
            Element placemark = new Element("Placemark");
            Element style = new Element("Style");
            style.addContent(new Element("LineStyle").addContent(new Element("color").setText("ff0000ff")));
            style.addContent(new Element("PolyStyle").addContent(new Element("fill").setText("0")));
            placemark.addContent(style);

            Element extendedData = new Element("ExtendedData");
            Element schemaData = new Element("SchemaData");
            schemaData.setAttribute(new Attribute("schemaUrl", "#SELECT"));
            schemaData.addContent(createSimpleData("ADMIN", "Aruba"));
            schemaData.addContent(createSimpleData("ISO_A3", "ABW"));

            extendedData.addContent(schemaData);
            placemark.addContent(extendedData);

            Element polygon = new Element("Polygon");
            polygon.addContent(new Element("outerBoundaryIs").addContent(new Element("LinearRing").addContent(new Element("coordinates").setText("71.0498022870001,38.4086644500001 71.05714034,38.4090261840001 71.0649434820001,38.411816712 71.076984091"))));

            if(!isMultiGeometry){
                Element multiGeometry = new Element("MultiGeometry");
                multiGeometry.addContent(polygon);
                placemark.addContent(multiGeometry);
                continue;
            }
            placemark.addContent(polygon);
            folder.addContent(placemark);
        }

        document.addContent(folder);
        root.addContent(document);
        doc.getRootElement().addContent(document);

        XMLOutputter xmlOutputer = new XMLOutputter();
        xmlOutputer.setFormat(Format.getPrettyFormat());
        try{
            xmlOutputer.output(document, new FileWriter("./test.kml"));
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }*/
    }
}
