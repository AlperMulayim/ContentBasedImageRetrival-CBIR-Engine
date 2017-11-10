package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import vpt.ByteImage;
import vpt.algorithms.io.Load;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Main extends Application {

    private  Parent root;
    private  Button btnSearch;
    private  Button btnLoad;
    private Button btnListView;
    private Text txtViewLog;
    private ImageView imgViewOrg;
    private  ImageView getImgViewMostSimilar;
    private ListView listView;
    private  CheckBox checkBoxManathan;
    private CheckBox checkBoxEuclide;
    private CheckBox checkHusMoment;
    private Text txtViewOper;
    private  ProgressBar progressBar;
    private  Text txtViewProgress;
    private  int complatedImages;
    private int totalImages;
    private FotoProperty userFotoProperty;
    private  FeatureVector userFeatureVec;
    static ScheduledExecutorService progressExecutor = Executors.newSingleThreadScheduledExecutor();

    private File[] fileArr;
    private Scene scene;
    private TableView<FotoProperty> tableView;
    private  List<FotoProperty> imgList;
    private boolean progStart  = true;
    private  Image imgSearch;
    int alper = 0;
    int i  = 2;
    private  ExecutorService executor;
    private  List<FeatureVector> husVectorList;


    public static  void main(String[] args) {
        launch(args);



        String userPath = System.getProperty("user.dir");
//        vpt.Image im = Load.invoke(userPath + "\\testIm.png");
//        HusMomentCalculator myCal = new HusMomentCalculator(im);
//        myCal.calculateMoment(0,0);
//        System.out.println(myCal.getHusVector());
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Creating an image
        husVectorList = new LinkedList<>();
        imgList = new LinkedList<>();
        root =  FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ImageFXTest");
        scene = new Scene(root, 950,650);
        scene.getStylesheets().add("sampleSty.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();



        preperareButtons();
        prepareTxtLog();
        prepareImageViews();
        //preperareListView();
        prepareCheckBoxes();
        prepareProgressBar();
    }

    public void prepareTableView(){

        //data column show data
        //Name Column
        TableColumn<FotoProperty,String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(80);
        nameColumn.setCellValueFactory(new PropertyValueFactory<FotoProperty, String>("label"));

        TableColumn<FotoProperty,Double> distanceCol = new TableColumn<>("Distance");
        distanceCol.setMinWidth(80);
        distanceCol.setCellValueFactory(new PropertyValueFactory<FotoProperty, Double>("euclidian"));


        TableColumn<FotoProperty,Image> imageColumn = new TableColumn<>("Image");
        imageColumn.setMinWidth(120);
        imageColumn.setCellValueFactory(new PropertyValueFactory<FotoProperty, Image>("img"));


        imageColumn.setCellFactory(new Callback<TableColumn<FotoProperty,Image>,TableCell<FotoProperty,Image>>(){
            @Override
            public TableCell<FotoProperty, Image> call(TableColumn<FotoProperty, Image> param) {
                TableCell<FotoProperty, Image> cell = new TableCell<FotoProperty, Image>(){
                    @Override
                    public void updateItem(Image item, boolean empty) {
                        if(item!=null){
                            HBox box= new HBox();
                            box.setSpacing(10) ;
                            VBox vbox = new VBox();


                            ImageView imageview = new ImageView();
                            imageview.setFitHeight(80);
                            imageview.setFitWidth(80);
                            imageview.setImage(item);

                            box.getChildren().addAll(imageview,vbox);
                            //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }

        });


        tableView = (TableView) root.lookup("#tableView");


        tableView.setItems(getImages());
        if(progStart == true) {
            progStart = false;
            tableView.getColumns().addAll(imageColumn, nameColumn,distanceCol);
        }
    }


    public void prepareProgressBar(){
        progressBar = (ProgressBar) root.lookup("#idProgressBar");

    }
    public ObservableList<FotoProperty> getImages(){
        ObservableList <FotoProperty> photoList = FXCollections.observableArrayList();

//        photoList.add(new FotoProperty("mağusa",imgSearch));
//        photoList.add(new FotoProperty("lefkoşa",imgSearch));

        for(int i = 0 ; i < imgList.size(); ++i){
            for(int j = i+ 1 ; j< imgList.size(); ++j){
                if(imgList.get(i).getEuclidian() < imgList.get(j).getEuclidian()){
                    FotoProperty temp = imgList.get(j);
                    imgList.set(j,imgList.get(i));
                    imgList.set(i,temp);
                }
            }
        }

        for(int  i = 0 ; i< 21; ++i){
            photoList.add(imgList.get(i));
        }
        return  photoList;
    }
    public void preperareButtons(){
        btnSearch = ((Button)root.lookup("#btnSearch"));
        btnLoad = ((Button)root.lookup("#btnLoad"));
        btnListView = ((Button) root.lookup("#btnListView"));
        buttonActions();
    }

    public  void prepareCheckBoxes(){
        checkBoxEuclide = ((CheckBox) root.lookup("#chBoxEuclide"));
        checkBoxManathan = ((CheckBox) root.lookup("#chBoxManathan"));
        checkHusMoment = (CheckBox) root.lookup("#checkHus");
    }
    public void preperareListView(){
        listView = ((ListView) root.lookup("#listView"));
        listView.getItems().addAll("Lefke","Mağusa","Girne");
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void buttonActions(){

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("Load");
                txtViewLog.setText("Search Image Loaded");



                try {
                    loadImage();
                } catch (FileNotFoundException e) {
                    txtViewLog.setText("File Cannot Load");
                }
                catch (NullPointerException e){
                    txtViewLog.setText("Image Cannot Load");
                }
            }
        });

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    loadSRCDir();


                } catch (FileNotFoundException e) {
                    txtViewLog.setText("SRC Directory not Found");
                }

            }
        });

        btnListView.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {


                progressExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(complatedImages != 0){
                                    progressBar.setProgress((double) complatedImages / totalImages);
                                    txtViewProgress.setText("Image : " + "( " +  complatedImages + " ) " + imgList.get(complatedImages -1 ).getLabel());

                                    txtViewLog.setText("Hus Vector : " +  husVectorList.get(complatedImages -1 ));
                                }
                            }
                        });

                    }
                },0,10,TimeUnit.MILLISECONDS);


                StringBuilder strB = new StringBuilder();

                System.out.println("Search");
                txtViewLog.setText("Search Button");

                if(checkBoxManathan.isSelected()){
                    strB.append("Manathan Distance");
                }
                strB.append(" - ");
                if(checkHusMoment.isSelected()){

                    strB.append("Hu's Moment ");
                    calculateHusMoments();

                }
                txtViewOper.setText(strB.toString());
                prepareTableView();
            }

        });

    }

    public void prepareImageViews(){
        imgViewOrg = ((ImageView) root.lookup("#imgViewOrg"));
        getImgViewMostSimilar = ((ImageView) root.lookup("#imgViewMostSimilar"));
    }

    public  void prepareTxtLog(){
        txtViewLog = ((Text) root.lookup("#txtViewLog"));
        txtViewProgress = (Text) root.lookup("#textProgress");
        txtViewOper = (Text) root.lookup("#txtOperationView");
    }


    public Image loadImage() throws FileNotFoundException , NullPointerException{
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Image image = new Image(new FileInputStream(file.getAbsolutePath()));

        imgSearch = image;
        txtViewProgress.setText("Image Loaded : " + file.getName());
        imgViewOrg.setImage(imgSearch);
        vpt.Image vptIm = Load.invoke(file.getAbsolutePath());
        userFotoProperty= new FotoProperty("UserImage",image,vptIm);
        calculateUserImageMoment();

        txtViewLog.setText("UserImage FE Vector " + userFeatureVec.toString());


        return  imgSearch;
    }

    public void loadSRCDir() throws FileNotFoundException {

        DirectoryChooser directChooser  = new DirectoryChooser();
        File dir = directChooser.showDialog(null);

        if(dir != null) {
            if (dir.isDirectory()) {
                fileArr = dir.listFiles();
                for (File f : fileArr) {
                   // System.out.println(f.getName());
                    Image img = new Image(new FileInputStream(f.getAbsolutePath()));
                    vpt.Image vptIm = Load.invoke(f.getAbsolutePath());
                    imgList.add(new FotoProperty(f.getName(), img, vptIm));
                }

                txtViewLog.setText( "( " + fileArr.length + " ) " + " Image Loaded from  " + dir.getName());
            }
        }
    }

    public void calculateUserImageMoment(){
        FeatureExtractor featureExtractor = new HusMomentCalculator(userFotoProperty.getVptImg(),"UserImage");
        featureExtractor.calculate();
        userFeatureVec = featureExtractor.getVector();

    }
    public void calculateHusMoments(){

       executor = Executors.newFixedThreadPool(4);

        List<Runnable> taskList = new ArrayList<>();
        totalImages = imgList.size();
        for(int i = 0 ; i < imgList.size(); ++i){
                int a = i;
                taskList.add(new Runnable() {
                    @Override
                    public void run() {
                        ++alper;
                        FeatureExtractor featureExtractor = new HusMomentCalculator(imgList.get(a).getVptImg(),imgList.get(a).getLabel());
                        featureExtractor.calculate();
                        EuclidianDistance euclidianDistance = new EuclidianDistance();
                        double eucdis = euclidianDistance.calculate(userFeatureVec,featureExtractor.getVector());
                        husVectorList.add(featureExtractor.getVector());
                        imgList.get(a).setEuclidian(eucdis);
                        ++complatedImages;
                    }
                });
        }

        for(Runnable task : taskList){
            executor.execute(task);
        }
        executor.shutdown();
    }

}
