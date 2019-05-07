import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeojsonReader {

    private final String filename;

    public List<Country> getCountryList() {
        return countryList;
    }

    private List<Country> countryList ;

    public GeojsonReader(String filename) {
        this.filename = filename;
        this.countryList = new ArrayList<>();
    }

    public void parse() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(this.filename)) {

            // lecture du fichier
            Object obj = jsonParser.parse(reader);

            JSONObject allData = (JSONObject) obj;

            // parcours du tableau de personnes
            JSONArray allFeatures = (JSONArray) allData.get("features");

            for (Object feature : allFeatures) {

                //System.out.println("Nouveau pays");
                JSONObject properties = (JSONObject) ((JSONObject) feature).get("properties");
                String name = (String) properties.get("ADMIN");
                String code = (String) properties.get("ISO_A3");
                Country country = new Country(name, code);
                //System.out.println(name + " " + code);

                JSONObject geometry = (JSONObject) ((JSONObject) feature).get("geometry");
                String typePolygon =  (String) geometry.get("type");

                if(typePolygon.equals("Polygon")){
                    //System.out.println("Polygon");
                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");
                    //System.out.println(allCoordinates);
                    JSONArray coordinateLevel2 = (JSONArray) allCoordinates.get(0);
                    //System.out.println(coordinateLevel2);
                    country.addDimension();
                    for(Object coordinates : coordinateLevel2){
                        //Pair pairCoordinate = new Pair((double)((JSONArray)coordinates).get(0),(double) ((JSONArray)coordinates).get(1));
                        CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),Double.toString((double)((JSONArray)coordinates).get(1)));
                        //System.out.println(pairCoordinate);
                        country.addCoordinate(pairCoordinate, 0);
                    }
                }else if(typePolygon.equals("MultiPolygon")){
                    //System.out.println("MultiPolygon");

                    JSONArray allCoordinates = (JSONArray) geometry.get("coordinates");

                    int index = 0;
                    for(Object multiCoord : allCoordinates){
                        country.addDimension();
                        //System.out.println(index);
                        JSONArray coordinateLevel2 = (JSONArray) ((JSONArray)multiCoord).get(0);
                        for(Object coordinates : coordinateLevel2){

                            CustomPair pairCoordinate = new CustomPair(Double.toString((double)((JSONArray)coordinates).get(0)),Double.toString((double)((JSONArray)coordinates).get(1)));
                            country.addCoordinate(pairCoordinate,index);
                            //System.out.println(pairCoordinate);
                        }
                        index++;

                    }
                }
                this.countryList.add(country);

            }
            // Affiche tous les pays et le nombre de ses coordonnees
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

