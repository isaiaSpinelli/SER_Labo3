import java.util.List;

/**
 * Classe permettant d'initialiser la lecture du fichier geojson et son écriture dans un fichier kml généré.
 * Elle servira ensuite à être chargée sur Google Earth afin de voir apparaître les frontières des pays entournées
 * en blancs
 *
 * Autheurs: Sutcu Volkan, Spinelli Isaia et Poulard Rémi
 */
class Main {
    public static void main(String[] args) {

        // Lecture du fichier Geojson et parsage
        GeojsonReader georeader = new GeojsonReader("countries.geojson");
        georeader.parse();
        // On en retire la liste de pays
        List<Country> countryList = georeader.getCountryList();

        // Création et écriture du fichier kml
        KMLWriterJDOM kml = new KMLWriterJDOM();
        kml.addCountries(countryList);
        kml.writeFile("src/export.kml");
    }
}
