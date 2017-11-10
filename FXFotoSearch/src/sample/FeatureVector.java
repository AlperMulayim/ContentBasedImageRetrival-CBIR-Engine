package sample;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alper on 3.11.2017.
 */
public class FeatureVector {
    private List<Double> vector;
    private String name;

    public FeatureVector(String name) {
        vector = new LinkedList<>();
        this.name = name;
    }

    public List<Double> getVector() {
        return vector;
    }
    public String getVectorName(){
        return name;
    }

    public void setVector(List<Double> vector, String name) {
        this.vector = vector;
    }

    public void add(Double elm){
        vector.add(elm);
    }
    public String toString(){

        return "[" + name + " : "  + vector.toString() + "]";
    }
}
