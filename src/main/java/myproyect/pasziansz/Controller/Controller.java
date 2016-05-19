package myproyect.pasziansz.Controller;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import myproyect.pasziansz.Model.Kartya;
import myproyect.pasziansz.Model.Pakli;

public class Controller implements Initializable {
    
    private Pakli pakli;
    private Kartya draggedKartya;
    private int prevDragged;
    
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
    
    private AnchorPane[] osszOszlop;
    private ImageView[] osszHalom;
    
    private boolean allowDrop;
    
    @FXML
    private void randomAction(MouseEvent event) {
        if(pakli.getCurrentRandom() >=0){
            Kartya seged = pakli.getKartyak().get(pakli.getCurrentRandom()) ;
            seged.setVisible(false);
            seged.setPlaceID(1);
        }
        Kartya seged = randomKarty();
        randomKartyaView.setImage( seged.getFace());
        refreshView();
        
    }
    
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
        
        osszHalom = new ImageView[4];
        osszHalom[0] = halom1View;
        osszHalom[1] = halom2View;
        osszHalom[2] = halom3View;
        osszHalom[3] = halom4View;
        
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
        seged = randomKarty();
        
        
        
        setupRandomKartyaListener(randomKartyaView);
        for (int i = 0; i < 7; i++) {
            setupOszlopListener(osszOszlop[i], i);
            if (i<4){
                setupHalomListener(osszHalom[i], i);
            }
        }
        
        
        
        refreshView();
    }
    
    
    private Kartya randomKarty(){
        
        
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
        seged.setPlaceID(1);
        seged.setVisible(true);
        
        return pakli.getKartyak().get(r); 
    }
    
    private Kartya kartyaHuzas(){
        boolean valid = true;
        int r;
        do{
            
            r = new Random().nextInt(51);
            if (pakli.getKartyak().get(r).getPlaceID().equals(1))
                valid = false;
        }while ( valid);
        return pakli.getKartyak().get(r);
    }
    
    private void refreshView(){
        
        for (int i = 0;i<7;i++){
            osszOszlop[i].getChildren().clear();
            visszaRendezesOszlop(i);
            drawWithStream(i);
            if(i<4)
                drawHalom(i);
            randomKartyaView.setImage(pakli.getKartyak().get(pakli.getCurrentRandom()).getFace());
            
        }
        
    }
    
    private void drawHalom(int i){
        try{
            Optional<Kartya> a = pakli.getKartyak().stream()
                    .filter(w -> w.getPlaceID().equals(i+3))
                    .max((a1,a2)-> a1.getStackNumber().compareTo(a2.getStackNumber()));
            Kartya item = a.get();
            ImageView Image = new ImageView(item.getFace());
            Image.setFitWidth(125);
            Image.setPreserveRatio(true);
            Image.setLayoutY(30*item.getStackNumber()+2);
            Image.setLayoutX(2);
            osszHalom[i].setImage(Image.getImage());
        }catch(NoSuchElementException e){}
                    
                
    }
    private void drawWithStream(int i){
        try{
            List<Kartya> lista= pakli.getKartyak().stream()
                    .filter(w -> w.getPlaceID().equals(i+7))
                    .sorted((a1, a2) -> Integer.compare(a1.getStackNumber(), a2.getStackNumber()))
                    .collect(Collectors.toList());
            lista.forEach(item -> {
                        ImageView Image;

                        if (item.isVisible()){
                            Image = new ImageView(item.getFace());
                            setupRandomKartyaListener(Image);
                        }
                        else 
                            Image = new ImageView(pakli.getHatlap());

                        Image.setFitWidth(125);
                        Image.setPreserveRatio(true);
                        Image.setLayoutY(30*item.getStackNumber()+2);
                        Image.setLayoutX(2);
                        osszOszlop[i].getChildren().add(Image);
                    });
        }catch(NoSuchElementException e){}
        
    }
    
    
    private void setupRandomKartyaListener(ImageView image){
        image.setOnDragDetected((event)->{
            draggedKartya = pakli.getKartyak().stream()
                           .filter(x-> x.getFace().equals(image.getImage()))
                           .findFirst()
                           .get();
            prevDragged = draggedKartya.getPlaceID();
            System.out.println("dragDetected: "+draggedKartya.getPlaceID()+" helyen "+draggedKartya.getStackNumber()+".");
            
            Dragboard dragBoard = image.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                content.putImage(image.getImage());

                dragBoard.setContent(content);
                   
        });
        image.setOnDragDone((event)->{
            randomKarty();
            refreshView();
            System.out.println("Done "+draggedKartya.getPlaceID());
            event.consume();
        
        });
    }
    private void setupOszlopListener(AnchorPane oszlop, int i){
        oszlop.setOnDragEntered((event)->{
            try{    
                Optional<Kartya> a = pakli.getKartyak().stream()
                        .filter(w -> w.getPlaceID().equals(i+7))
                        .max((a1,a2)-> a1.getStackNumber().compareTo(a2.getStackNumber()));
                allowDrop = false;

                if(a.get().getNumValue().equals(draggedKartya.getNumValue()+1))
                    switch(a.get().getType()){
                        case "diamonds":
                                if(draggedKartya.getType().equals("clubs") || draggedKartya.getType().equals("spades"))
                                    allowDrop = true;
                            break;
                        case "hearts":
                                if(draggedKartya.getType().equals("clubs") || draggedKartya.getType().equals("spades"))
                                    allowDrop = true;
                            break;
                        case "clubs":
                                if(draggedKartya.getType().equals("diamonds") || draggedKartya.getType().equals("hearts"))
                                    allowDrop = true;
                            break;
                        case "spades":
                                if(draggedKartya.getType().equals("diamonds") || draggedKartya.getType().equals("hearts"))
                                    allowDrop = true;
                            break;
                    }
                if(!a.get().isVisible())
                    allowDrop = true;
            }catch(NoSuchElementException e){
                allowDrop = true;
            }
            
            if (allowDrop)            
                oszlop.setBlendMode(BlendMode.BLUE);
            else oszlop.setBlendMode(BlendMode.GREEN);
        });
        oszlop.setOnDragExited((event)->{
            oszlop.setBlendMode(null);
            
            
        });
        oszlop.setOnDragOver((event)->{
            if (allowDrop)
            event.acceptTransferModes(TransferMode.MOVE);
            
        });
        oszlop.setOnDragDropped((event)->{
            Dragboard db = event.getDragboard();
            db.clear();
            
            int stackNumber;
            try{
                stackNumber = (int)pakli.getKartyak().stream()
                        .filter(w->w.getPlaceID().equals(i+7))
                        .count();
            }catch(NoSuchElementException e){
                stackNumber = 0;
            }
           
            if (draggedKartya.getPlaceID() > 2){
                 List<Kartya> athelyez = pakli.getKartyak().stream()
                        .filter(w-> (w.getPlaceID().equals(draggedKartya.getPlaceID()) && (w.getStackNumber() >= draggedKartya.getStackNumber())))
                        .collect(Collectors.toList());
                
                for(Kartya a : athelyez){
                    a.setPlaceID(i+7);
                    a.setStackNumber(stackNumber);
                    stackNumber = stackNumber + 1;
                    a.setVisible(true);
                }
            }
            else{
                draggedKartya.setPlaceID(i+7);
                draggedKartya.setStackNumber(stackNumber);
                draggedKartya.setVisible(true);
            }
            
            event.setDropCompleted(true);
            System.out.println("dropped place:"+(i+7)+" stack"+stackNumber);
            refreshView();
            System.out.println("dropped: "+draggedKartya.getPlaceID());
            
        });
        
    }
    
    
    private void setupHalomListener(ImageView halom, int i){
        halom.setOnDragEntered((event)->{
            allowDrop = false;
            try{
                Integer max = pakli.getKartyak().stream()
                            .filter(w -> w.getPlaceID().equals(draggedKartya.getPlaceID()))
                            .max((a1,a2)->a1.getStackNumber().compareTo(a2.getStackNumber())).get().getStackNumber();
                Kartya halomTop = pakli.getKartyak().stream()
                            .filter(w->w.getPlaceID().equals(i+3))
                            .max((a1,a2) -> a1.getStackNumber().compareTo(a2.getStackNumber())).get();
                if(draggedKartya.getType().equals(halomTop.getType()) && draggedKartya.getStackNumber().equals(max) && (draggedKartya.getNumValue() < halomTop.getNumValue()))
                    allowDrop = true;
            }catch(NoSuchElementException e){
                allowDrop = true;
            }
            
            if (allowDrop)            
                halom.setBlendMode(BlendMode.BLUE);
            else halom.setBlendMode(BlendMode.GREEN);
            
        });
        halom.setOnDragExited((event)->{
            halom.setBlendMode(null);
            
        });
        halom.setOnDragOver((event)->{
            event.acceptTransferModes(TransferMode.MOVE);
            
        });
        halom.setOnDragDropped((event)->{
            if(allowDrop){
                Dragboard db = event.getDragboard();
                db.clear();
                draggedKartya.setPlaceID(i+3);
                int stackNumber;
                try{
                    stackNumber = (int)pakli.getKartyak().stream()
                            .filter(w->w.getPlaceID().equals(i+3))
                            .count();
                }catch(NoSuchElementException e){
                    stackNumber = 0;
                }
                draggedKartya.setStackNumber(stackNumber);
                draggedKartya.setVisible(true);
                event.setDropCompleted(true);
                System.out.println("dropped place:"+(i+3)+" stack"+stackNumber);
            }
            refreshView();
            
        });
    }
    private void visszaRendezesOszlop(int i){
        try{
            
            List<Kartya> lista= pakli.getKartyak().stream()
                   .filter(w -> w.getPlaceID().equals(i+7))
                   .sorted((a1, a2) -> Integer.compare(a1.getStackNumber(), a2.getStackNumber()))
                   .collect(Collectors.toList());
            int j;
            for (j = 0; j< lista.size();j++){
                lista.get(j).setStackNumber(j);
            }
            lista.get(lista.size()-1).setVisible(true);
        }catch(Exception e){}
         
    }
    
}
