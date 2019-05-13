import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet d'instancier un objet représenant un pays. L'objet est identifié par son nom, son code et
 * sa liste de coordonnées
 */
public class Country {
    private String name;
    private String code;
    // Une liste de polygons, chacune contenant une liste de coordonnées du polygon par pair
    private List<List<CustomPair>> coordinates;

    /**
     * Permet d'instancier un objet représentant le pays
     * @param name le nom du pays
     * @param code le code du pays
     */
    public Country(String name, String code){
        this.name = name;
        this.code = code;
        this.coordinates = new ArrayList<>();
    }

    /**
     * Récupère le nom du pays
     * @return une chaîne de caractère représentant le nom du pays
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère le code du pays
     * @return une chaîne de caractère représentant le code du pays
     */
    public String getCode() {
        return code;
    }

    /**
     * Récupère la liste de polygon avec chacune, sa liste de coordonnées
     * @return la liste de toute les coordonnées
     */
    public List<List<CustomPair>> getCoordinates() {
        return coordinates;
    }

    /**
     * Permet d'ajouter un polygon supplémentaire
     */
    public void addDimension(){
        List<CustomPair> pair = new ArrayList<>();
        this.coordinates.add(pair);

    }

    /**
     * Permet d'ajouter des coordonnées
     * @param coordinate les coordonnées à ajouter
     * @param index la position à laquelle on l'ajoute dans la liste
     */
    public void addCoordinate(CustomPair coordinate, int index){
        this.coordinates.get(index).add(coordinate);
    }

    /**
     * Transforme les coordonnées en une chaîne de caractère unique
     * @return la chaîne complète représentant les coordonnées
     */
    private String coordinatesToString(){
        StringBuffer coord = new StringBuffer();
        for (List<CustomPair> listCountries : coordinates){
            coord.append( "\n\t- "+(listCountries.size())+" coordinates" );
        }
        return coord.toString();
    }

    /**
     * Affiche le code du pays, suivi de son nom et ses coordonnées
     * @return
     */
    public String toString(){
        return "("+this.code+") "+this.name+coordinatesToString();
    }
}
