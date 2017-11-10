package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Alper on 3.11.2017.
 */
public class FotoProperty {
    private String label;
    private Image img;
    private ImageView imgView;
    private vpt.Image vptImg;
    private double euclidian;



    public FotoProperty(String label, Image img, vpt.Image vptIm) {
        this.label = label;
        this.img = img;
        this.vptImg = vptIm;

        this.imgView = new ImageView();

        this.imgView.setFitHeight(50);
        this.imgView.setFitWidth(50);
        imgView.setImage(img);
        this.euclidian = -1;

    }

    public double getEuclidian() {
        return euclidian;
    }

    public void setEuclidian(double euclidian) {
        this.euclidian = euclidian;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }
    public vpt.Image getVptImg() {
        return vptImg;
    }

    public void setVptImg(vpt.Image vptImg) {
        this.vptImg = vptImg;
    }
}
