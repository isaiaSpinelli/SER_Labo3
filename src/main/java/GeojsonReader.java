import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet la lecture d'un fichier de type geojson et de le parser
 */
public class GeojsonReader {
    private final String filename;
    private List<Country> countryList ;

    /**
     * Récupère la liste de pays
     * @return la liste de pays
     */
    public List<Country> getCountryList() {
        return countryList;
    }

    /**
     * Instancie un lecteur de fichier geojson
     * @param filename le nom du fichier à lire
     */
    public GeojsonReader(String filename) {
        this.filename = filename;
        this.countryList = new ArrayList<>();
    }

    /**
     * Permet de parser le fichier qui est lu
     */
    public void parse() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(this.filename)) {

            // Lecture du fichier
            Object obj = jsonParser.parse(reader);

            // Création de notre objet JSON principal
            JSONObject allData = (JSONObject) obj;

            // Parcours du tableau de features
            JSONArray allFeatures = (JSONArray) allData.get("features");

            // Parcourt chaque objet du fichier geojson
            for (Object feature : allFeatures) {
                JSONObject properties = (JSONObject) ((JSONObject) feature).get("properties");

                // Recupération du nom et du code du pays
                String name = (String) properties.get("ADMIN");
                String code = (String) properties.get("ISO_A3");

                // Création du pays
                Country country = new Country(name, code);

                // Récuperation de notre objet "geometry"
                JSONObject geometry = (JSONObject) ((JSONObject) feature).get("geometry");

                // Permet de recuperer le type de polygon du pays
                String typePolygon =  (String) geometry.get("type");

                // Vérification du type de polygon pour avoir un comportement différent
                if(typePolygon.equals("Polygon")){
                    // Récupértion du tableau de tableau de coordonnées
                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");
                    // Récupération du tableau de coordonnées
                    JSONArray coordinateLevel2 = (JSONArray) allCoordinates.get(0);
                    // Ajout d'une ArrayListe<CustomPair> a notre liste de coordonnées
                    country.addDimension();
                    for(Object coordinates : coordinateLevel2){
                        // Création de la pair avec les deux coordonnées récupérées
                        CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),
                                Double.toString((double)((JSONArray)coordinates).get(1)));
                        country.addCoordinate(pairCoordinate, 0);
                    }
                }else if(typePolygon.equals("MultiPolygon")){
                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");

                    int index = 0;
                    for(Object multiCoord : allCoordinates){
                        // Pour chaque polygon du multipolgon, ajout d'une dimension de liste
                        country.addDimension();
                        // Récupération du tableau de coordonnées
                        JSONArray coordinateLevel2 = (JSONArray) ((JSONArray)multiCoord).get(0);
                        for(Object coordinates : coordinateLevel2){
                            // Création de la pair avec les deux coordonnées récupérées
                            CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),Double.toString((double)((JSONArray)coordinates).get(1)));
                            country.addCoordinate(pairCoordinate,index);
                        }
                        ++index;
                    }
                }
                this.countryList.add(country);

            }
            // Affiche tous les pays et le nombre de ses coordonnées
            for (Country country : countryList) {
                System.out.println(country);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}

