package sample;

import vpt.DoubleImage;
import vpt.Image;

/**
 * Hu's Moment Vector Algorithm from Udacity Computer Vision Lecture
 * https://www.youtube.com/watch?v=uEVrJrJfa0s
 * https://www.wikiwand.com/en/Image_moment
 * Created by Alper on 3.11.2017.
 */
public class HusMomentCalculator implements  FeatureExtractor {
    private Image  theImage;
    private FeatureVector featureVector;


    public HusMomentCalculator(Image theImage, String imageName) {
        //convert img to DoubleImage
        this.theImage = new DoubleImage(theImage,true);
        this.featureVector = new FeatureVector(imageName);
    }


    private double calculateMoment(int paramI, int paramJ){

        double result = 0;

        for(int x = 0 ; x < theImage.getXDim(); ++x){
            for(int y = 0 ; y< theImage.getYDim(); ++y){
                result +=( (Math.pow(x,paramI) * Math.pow(y,paramJ) * theImage.getXYDouble(x,y)));
            }
        }
        return result;
    }

    private double calculateHU1(){
        return calculateMoment(2,0) + calculateMoment(0,2);
    }

    private double calculateHU2(){
        return (Math.pow( calculateMoment(2,0) - calculateMoment(0,2),2) + 4 * Math.pow(calculateMoment(1,1),2));
    }

    private double calculateHU3(){
        return ( Math.pow(calculateMoment(3,0) - (3 * calculateMoment(0,2)),2) + Math.pow(4 * calculateMoment(1,1),2));
    }

    private double calculateHU4(){
        return (Math.pow(calculateMoment(3,0) + calculateMoment(1,2),2) + Math.pow(calculateMoment(2,1) + calculateMoment(0,3),2));
    }
    private double calculateHU5(){
        double a = calculateMoment(3,0);
        double b = calculateMoment(1,2);
        double c = calculateMoment(2,1);
        double d = calculateMoment(0,3);
        double x = ((a - 3 * b ) * (a + b) * ((Math.pow(a + b ,2)  - (3 * Math.pow(c + d,2)))));
        double y = ((3 * c - d) * (c + d) * (Math.pow(a + b,2) - (Math.pow(c+d,2))));

        return x + y;
    }

    private double calculateHU6(){
        double a = calculateMoment(2,0);
        double b = calculateMoment(0,2);
        double c = calculateMoment(3,0);
        double d = calculateMoment(1,2);
        double e = calculateMoment(2,1);
        double f = calculateMoment(0,3);
        double g = calculateMoment(1,1);
        double x = a -b;
        double y = ( Math.pow(c + d ,2) - Math.pow(e + f , 2));
        double z = g * (c + d ) * (e + f);

        return x * y + 4 * z;

    }

    private  double calculateU7(){
        double a = calculateMoment(2,1);
        double b = calculateMoment(0,3);
        double c = calculateMoment(3,0);
        double d = calculateMoment(1,2);

        double x = (3 * a - b) * (c + d ) * ((Math.pow(c + d,2) - 3 * Math.pow(d + b,2)));
        double y = (c - 3 * d) * ( a + b) *((3*Math.pow(c+d ,2)) - Math.pow(a+b,2));
        return x - y;

    }

    @Override
    public void calculate(){

        featureVector.add(calculateHU1());
        featureVector.add(calculateHU2());
        featureVector.add(calculateHU3());
        featureVector.add(calculateHU4());
        featureVector.add(calculateHU5());
        featureVector.add(calculateHU6());
        featureVector.add(calculateU7());
    }

    @Override
    public FeatureVector getVector(){
        return featureVector;
    }

}
