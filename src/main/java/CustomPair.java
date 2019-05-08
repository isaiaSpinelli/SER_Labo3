/**
 * Cette classe permet d'instancier les paires de coordonnées et d'accéder à sa première ou seconde valeur
 */
public class CustomPair {
    private String first;
    private String second;

    /**
     * Instancie une paire de coordonnées
     * @param first première valeur de la paire
     * @param second seconde valeur de la paire
     */
    public CustomPair(String first, String second){
        this.first = first;
        this.second = second;
    }

    /**
     * Permet de récupérer la première valeur de la paire
     * @return la première valeur de la paire
     */
    public String getFirst() {
        return first;
    }

    /**
     * Permet de récupérer la seconde valeur de la paire
     * @return la seconde valeur de la paire
     */
    public String getSecond() {
        return second;
    }

    /**
     * Affiche la coordonnée via sa valeur de gauche et de droite
     * @return
     */
    public String toString(){
        return this.first +","+this.second;
    }
}
