import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;

    private List<List<CustomPair>> coordinates;

    public Country(String name){
        this.name = name;
        this.coordinates = new ArrayList<>();
    }

    public String getName() {
        return name;
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
}
