public class CustomPair {

    private String first;
    private String second;

    public CustomPair(String first, String second){
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String toString(){
        return this.first +","+this.second;
    }
}
