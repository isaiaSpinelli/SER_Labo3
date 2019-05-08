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

            JSONObject allData = (JSONObject) obj;

            // Parcours du tableau de personnes
            JSONArray allFeatures = (JSONArray) allData.get("features");

            // Parcourt chaque objet du fichier geojson
            for (Object feature : allFeatures) {
                JSONObject properties = (JSONObject) ((JSONObject) feature).get("properties");
                String name = (String) properties.get("ADMIN");
                String code = (String) properties.get("ISO_A3");
                Country country = new Country(name, code);

                JSONObject geometry = (JSONObject) ((JSONObject) feature).get("geometry");
                String typePolygon =  (String) geometry.get("type");

                // Vérifie qu'il y ait un polygon et non un multipolygon
                if(typePolygon.equals("Polygon")){
                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");
                    JSONArray coordinateLevel2 = (JSONArray) allCoordinates.get(0);
                    country.addDimension();
                    for(Object coordinates : coordinateLevel2){
                        CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),Double.toString((double)((JSONArray)coordinates).get(1)));
                        country.addCoordinate(pairCoordinate, 0);
                    }
                }else if(typePolygon.equals("MultiPolygon")){
                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");

                    int index = 0;

                    for(Object multiCoord : allCoordinates){
                        country.addDimension();
                        JSONArray coordinateLevel2 = (JSONArray) ((JSONArray)multiCoord).get(0);
                        for(Object coordinates : coordinateLevel2){

                            CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),Double.toString((double)((JSONArray)coordinates).get(1)));
                            country.addCoordinate(pairCoordinate,index);
                        }
                        ++index;
                    }
                }
                this.countryList.add(country);

            }
            // Affiche tous les pays et le nombre de ses coordonnées
            for (Country country : countryList)
                System.out.println(country);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

