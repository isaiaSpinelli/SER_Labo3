import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;
    private String code;

    private List<List<CustomPair>> coordinates;

    public Country(String name, String code){
        this.name = name;
        this.code = code;
        this.coordinates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<List<CustomPair>> getCoordinates() {
        return coordinates;
    }

    public void addDimension(){
        List<CustomPair> pair = new ArrayList<>();
        this.coordinates.add(pair);

    }

    public void addCoordinate(CustomPair coordinate, int index){
        this.coordinates.get(index).add(coordinate);
    }

    public String coordinatesToString(){

        StringBuffer coord = new StringBuffer();

        for (List<CustomPair> listCountries : coordinates){
            coord.append( "\n\t- "+(listCountries.size())+" coordinates" );
        }

        return coord.toString();

    }

    public String toString(){

        return "("+this.code+") "+this.name+coordinatesToString();
    }
}
