package myproyect.pasziansz.Controller;

import java.net.URL;
import java.util.Collections;
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

    Pakli pakli; // a modell
    
    // A View-ban található elemek leképezése-----------------------------------
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
    
    //--------------------------------------------------------------------------
    
    //a View elemeit csoportosító tömbök, a könnyebb kezelhetőség érdekében-----
    private AnchorPane[] osszOszlop;
    private ImageView[] osszHalom;
    //--------------------------------------------------------------------------
    
    private int draggedIngex;
    
    
    /**
     * Itt kerül inicializálásra a játék.
     * Létre jön a pakli objektum, mely a modell szerepéül szolgál.
     * Továbbá a View elemeihez itt hozom létre a Listenereket.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Modell inicializálása
        pakli = new Pakli();
        //a tábla oszlop referenciáinak inicializálása az erre alkalmas tömbben
        osszOszlop = new AnchorPane[7];
        osszOszlop[0] = oszlop1View;
        osszOszlop[1] = oszlop2View;
        osszOszlop[2] = oszlop3View;
        osszOszlop[3] = oszlop4View;
        osszOszlop[4] = oszlop5View;
        osszOszlop[5] = oszlop6View;
        osszOszlop[6] = oszlop7View;
        //a tábla halom referenciáinak inicializálása az erre alkalmas tömbben
        osszHalom = new ImageView[4];
        osszHalom[0] = halom1View;
        osszHalom[1] = halom2View;
        osszHalom[2] = halom3View;
        osszHalom[3] = halom4View;
        
        pakliView.setImage(pakli.getHatlap());
        
        halom1View.setImage(pakli.getHatlap());
        halom2View.setImage(pakli.getHatlap());
        halom3View.setImage(pakli.getHatlap());
        halom4View.setImage(pakli.getHatlap());
        
        this.athelyezKartya(this.randomKartyIndex(), 2);
        
        //Listenerek beállítása
        kartyaListeners(randomKartyaView);
        for(int i = 0;i<7;i++){
            oszlopListeners(osszOszlop[i], i);
            if(i<4){}
        }
        pakliView.setOnMouseClicked(event->{
            this.csereKratya();
            refreshView();
        });
        
    }
    /**
     * Eldönti, hogy a kiválasztott kártyát át lehet e helyezni a kívánt helyre
     * 
     * 
     * @param index a kártya indexe a pakliban
     * @param hely a cél helyét jelölő index
     * @return jelzi, hogy az áthelyezés sikeres volt e
     */
    private boolean athelyezHitelesites(int index, int hely){
        Kartya akt = pakli.getKartyak().get(index);
        try{
            Kartya celMax = pakli.getKartyak().stream()
                    .filter(w->w.getPlaceID().equals(hely))
                    .max((a1,a2)->a1.getStackNumber().compareTo(a2.getStackNumber())).get();
            
            //ha oszlop
            if(hely > 6){
                boolean allowDrop = false;
                if(celMax.getNumValue().equals(akt.getNumValue()+1))
                    switch(celMax.getType()){
                        case "diamonds":
                                if(akt.getType().equals("clubs") || akt.getType().equals("spades"))
                                    allowDrop = true;
                            break;
                        case "hearts":
                                if(akt.getType().equals("clubs") || akt.getType().equals("spades"))
                                    allowDrop = true;
                            break;
                        case "clubs":
                                if(akt.getType().equals("diamonds") || akt.getType().equals("hearts"))
                                    allowDrop = true;
                            break;
                        case "spades":
                                if(akt.getType().equals("diamonds") || akt.getType().equals("hearts"))
                                    allowDrop = true;
                            break;
                    }
              return allowDrop;
            }
            //ha halom
            else if (hely > 2){
                return celMax.getNumValue().equals(akt.getNumValue()+1) && celMax.getType().equals(akt.getType());
            }
            else return true;
        }catch(Exception e){
            if(hely > 6){
                return true;
            }
            else if(hely >2){
                return akt.getValue().equals("king");
            }
            else return true;
        }
    }
    
    
    /**
     * Ezzel a metódussal át tudunk helyezni egy kártyát egy másik helyre (oszlop, halom...)
     * 
     * 
     * @param index a kártya indexe a pakliban
     * @param hely a cél helyét jelölő index
     * @return jelzi, hogy az áthelyezés sikeres volt e
     */
    private boolean athelyezKartya(int index, int hely){
        try{
            Kartya akt = pakli.getKartyak().get(index);
            int stackNum;
            try{
                if(hely>2)
                    stackNum = pakli.getKartyak().stream()
                            .filter(w->w.getPlaceID().equals(hely))
                            .max((a1,a2)->a1.getStackNumber().compareTo(a2.getStackNumber())).get().getStackNumber()+1;
                else stackNum = 0;
            }catch(Exception e){
                stackNum = 0;

            }
            akt.setPlaceID(hely);
            akt.setStackNumber(stackNum);
            if(hely == 1){
                akt.setVisible(false);
            }
            else akt.setVisible(true);
            
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    /**
     * Ha a felhasználó nem tud mit kezdeni a pakliból húzott lappal,
     * akkor ez a függvény intézi el, hogy az a lap vissza kerüljön a pakliba,
     * és egy másik kerüljön a helyére.
     * 
     * @return az 
     */
    private boolean csereKratya(){
        try{
            Kartya akt = pakli.getKartyak().stream()
                    .filter(w->w.getPlaceID().equals(2))
                    .findFirst().get();
            akt.setPlaceID(1);
            akt.setStackNumber(0);
            akt.setVisible(false);
        }catch(Exception e){}
        
        try{
            athelyezKartya(this.randomKartyIndex(), 2);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     * kiválaszt egy kártyát ami még a pakliban van
     * @return a kiválasztott kártya indexe
     */
    private int randomKartyIndex(){
        List<Kartya> maradek = pakli.getKartyak().stream()
                .filter(w->w.getPlaceID().equals(1))
                .collect(Collectors.toList());
        Random r = new Random();
        int i;
        do{
            i = r.nextInt(maradek.size());
        }while (!maradek.get(i).getPlaceID().equals(1));
        
        return maradek.get(i).getIndex();
    }
    
    private void kartyaListeners(ImageView image)
    {
        image.setOnDragDetected((event)->{
            draggedIngex = pakli.getKartyak().stream()
                           .filter(x-> x.getFace().equals(image.getImage()))
                           .findFirst()
                           .get().getIndex();
            
                Dragboard dragBoard = image.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                content.putImage(image.getImage());

                dragBoard.setContent(content);
                
                event.consume();
                   
        });
        image.setOnDragDone((event)->{
            if(pakli.getKartyak().stream().filter(w->w.getPlaceID().equals(2)).count() == 0)
                this.athelyezKartya(this.randomKartyIndex(), 2);
            
            
            event.consume();
            refreshView();
        });
    }
    private void oszlopListeners(AnchorPane oszlop, int i){
        oszlop.setOnDragEntered((event)->{
            if (this.athelyezHitelesites(draggedIngex, i+7))            
                oszlop.setBlendMode(BlendMode.BLUE);
            else oszlop.setBlendMode(BlendMode.GREEN);
            event.consume();
        });
        oszlop.setOnDragExited((event)->{
            oszlop.setBlendMode(null);
            
            event.consume();
        });
        oszlop.setOnDragOver((event)->{
            if (oszlop.getBlendMode().equals(BlendMode.BLUE))
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        oszlop.setOnDragDropped((event)->{
            Dragboard db = event.getDragboard();
            db.clear();
            
            if(oszlop.getBlendMode().equals(BlendMode.BLUE)){
                List<Kartya> indexek = pakli.getKartyak().stream()
                        .filter(w->w.getPlaceID().equals(pakli.getKartyak().get(draggedIngex).getPlaceID()) && (w.getStackNumber()>=pakli.getKartyak().get(draggedIngex).getStackNumber()))
                        .sorted((a1,a2)-> a1.getStackNumber().compareTo(a2.getStackNumber()))
                        .collect(Collectors.toList());
                
                indexek.forEach(item->{
                    this.athelyezKartya(item.getIndex(), i+7);
                });
                
                
            }
            
            event.setDropCompleted(true);
            
            event.consume();
        });
        
    }
    private void refreshView(){
        //oszlopok frissítése
        for(int i=0;i<7;i++){
            osszOszlop[i].getChildren().clear();
            drawOszlop(i);
            if(i<4){
                drawHalom(i);
            }
            try {
                randomKartyaView.setImage(pakli.getKartyak().stream().filter(w->w.getPlaceID().equals(2)).findFirst().get().getFace());
            } catch (Exception e) {
                System.out.println("myproyect.pasziansz.Controller.Controller2.refreshView() -.-.-.-.-.-.-.-.-.");
            }
                
         
        }
        
    }
    private void drawOszlop(int i){
        try{
            List<Kartya> lista= pakli.getKartyak().stream()
                    .filter(w -> w.getPlaceID().equals(i+7))
                    .sorted((a1, a2) -> Integer.compare(a1.getStackNumber(), a2.getStackNumber()))
                    .collect(Collectors.toList());
            lista.forEach(item -> {
                        ImageView Image;

                        if (item.isVisible()){
                            Image = new ImageView(item.getFace());
                            kartyaListeners(Image);
                        }
                        else 
                            Image = new ImageView(pakli.getHatlap());

                        Image.setFitWidth(125);
                        Image.setPreserveRatio(true);
                        Image.setLayoutY(30*item.getStackNumber()+2);
                        Image.setLayoutX(2);
                        osszOszlop[i].getChildren().add(Image);
                    });
        }catch(Exception e){}
    }
    private void drawHalom(int i){
        try{
            Kartya item = pakli.getKartyak().stream()
                    .filter(w -> w.getPlaceID().equals(i+3))
                    .max((a1,a2)-> a1.getStackNumber().compareTo(a2.getStackNumber())).get();
            ImageView Image = new ImageView(item.getFace());
            Image.setFitWidth(125);
            Image.setPreserveRatio(true);
            Image.setLayoutY(30*item.getStackNumber()+2);
            Image.setLayoutX(2);
            osszHalom[i].setImage(Image.getImage());
        }catch(Exception e){}
                    
                
    }
}