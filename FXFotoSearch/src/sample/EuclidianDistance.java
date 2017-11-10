package sample;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by Alper on 5.11.2017.
 */
public class EuclidianDistance {



    public double calculate(FeatureVector userVec, FeatureVector img){
        double total = 0;


            List<Double> userV = userVec.getVector();
            List<Double> tempV = img.getVector();


            for(int j  =  0 ; j< userV.size(); ++j){
                total += Math.pow((userV.get(j) - tempV.get(j)), 2);
            }

            return Math.sqrt(total);
        }

}
