package myproyect.pasziansz.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Collation;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import static jdk.nashorn.internal.objects.NativeRegExp.source;
import myproyect.pasziansz.Model.Kartya;
import myproyect.pasziansz.Model.Pakli;

public class Controller implements Initializable {
    
    private Pakli pakli;
    private Kartya draggedKartya;
    private ImageView draggedImageView;
    
  /*  
    @FXML
    private void handleButtonAction(ActionEvent event) {
        String ID = field.getText();
        Image kep = pakli.getKartyak().get(Integer.parseInt(ID)).getFace();
        ImageView image;
        image = new ImageView(kep);
        image.setFitWidth(150);
        image.setPreserveRatio(true);
        image.setLayoutX(x+=10);
        image.setLayoutY(y);
        setupDragDropListener(image);
        anchorpane.getChildren().add(image);
        
    }
    */
    
    @FXML
    private void randomAction(MouseEvent event) {
        randomKartyaView.setImage( kartyaHuzas().getFace());
    }
    
    
    @FXML
    private ImageView pakliView;
    @FXML
    private ImageView randomKartyaView;
    @FXML
    private ImageView halom1View;
    @FXML
    private ImageView halom2View;
    @FXML
    private ImageView halom3View;
    @FXML
    private ImageView halom4View;
    @FXML
    private AnchorPane oszlop1View;
    @FXML
    private AnchorPane oszlop2View;
    @FXML
    private AnchorPane oszlop3View;
    @FXML
    private AnchorPane oszlop4View;
    @FXML
    private AnchorPane oszlop5View;
    @FXML
    private AnchorPane oszlop6View;
    @FXML
    private AnchorPane oszlop7View;
    @FXML
    private AnchorPane root;
    
    private AnchorPane[] osszOszlop;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        osszOszlop = new AnchorPane[7];
        osszOszlop[0] = oszlop1View;
        osszOszlop[1] = oszlop2View;
        osszOszlop[2] = oszlop3View;
        osszOszlop[3] = oszlop4View;
        osszOszlop[4] = oszlop5View;
        osszOszlop[5] = oszlop6View;
        osszOszlop[6] = oszlop7View;
        
        
        pakli = new Pakli();
        pakliView.setImage(pakli.getHatlap());
        
        halom1View.setImage(pakli.getHatlap());
        halom2View.setImage(pakli.getHatlap());
        halom3View.setImage(pakli.getHatlap());
        halom4View.setImage(pakli.getHatlap());
        
        Kartya seged;
        for(int i = 0;i<7;i++){
            for(int j = 0;j<i;j++){
                seged = kartyaHuzas();
                seged.setVisible(false);
                seged.setPlaceID(i+7);
                seged.setStackNumber(j);
            }
            seged = kartyaHuzas();
                seged.setVisible(true);
                seged.setPlaceID(i+7);
                seged.setStackNumber(i);
        }
        
        randomKartyaView.setImage(kartyaHuzas().getFace());
        
        
        setupRandomKartyaListener(randomKartyaView);
        
        root.setOnDragOver((event)->{
            System.out.println("DragOver");
            draggedImageView.setLayoutX(event.getSceneX());
            draggedImageView.setLayoutY(event.getSceneY());
        });
        
        
        
        refreshView();
    }
    
    
    private Kartya randomKarty(){
        if(pakli.getCurrentRandom() >=0){
            pakli.getKartyak().get(pakli.getCurrentRandom()).setVisible(false);
        }
        
        boolean valid = true;
        int r;
        do{
            
            r = new Random().nextInt(51);
            if (pakli.getKartyak().get(r).getPlaceID() == 1)
                valid = false;
        }while ( valid);
        
        pakli.setCurrentRandom(r);
        Kartya seged = pakli.getKartyak().get(pakli.getCurrentRandom());
        seged.setPlaceID(2);
        
        return pakli.getKartyak().get(r); 
    }
    
    private Kartya kartyaHuzas(){
        boolean valid = true;
        int r;
        do{
            
            r = new Random().nextInt(51);
            if (pakli.getKartyak().get(r).getPlaceID() == 1)
                valid = false;
        }while ( valid);
        
        return pakli.getKartyak().get(r);
    }
    
    private void refreshView(){
        for (int i = 0;i<7;i++){
            drawWithStream(i);
        }
        
    }
    private void drawWithStream(int i){
        List<Kartya> lista= pakli.getKartyak().stream()
                .filter(w -> w.getPlaceID().equals(i+7))
                .sorted((a1, a2) -> Integer.compare(a1.getStackNumber(), a2.getStackNumber()))
                .collect(Collectors.toList());
        lista.forEach(item -> {
                    ImageView Image;
                    
                    if (item.isVisible())
                        Image = new ImageView(item.getFace());
                    else 
                        Image = new ImageView(pakli.getHatlap());
                    
                    Image.setFitWidth(125);
                    Image.setPreserveRatio(true);
                    Image.setLayoutY(30*item.getStackNumber());
                    Image.setLayoutX(0);
                    osszOszlop[i].getChildren().add(Image);
                });
        
    }
    
    private void setupRandomKartyaListener(ImageView image){
        image.setOnDragDetected((event)->{
            draggedImageView = image;
            draggedKartya = pakli.getKartyak().stream()
                           .filter(x-> x.getFace().equals(image.getImage()))
                           .findAny()
                           .get();
            System.out.println("dragDetected: "+draggedKartya.getPlaceID()+" helyen "+draggedKartya.getStackNumber()+".");
                   
        });
    }
    
}
