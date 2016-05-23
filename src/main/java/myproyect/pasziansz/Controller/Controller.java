package myproyect.pasziansz.Controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import myproyect.pasziansz.MainApp;
import myproyect.pasziansz.Model.Kartya;
import myproyect.pasziansz.Model.Pakli;
import org.slf4j.*;

/**
 * Ez a játék Controllere
 * Ez köti össze a Model-t a View-val
 * 
 * @author csermely
 */
public class Controller implements Initializable {
    
    static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

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
        System.out.println(url.toString());
        MainApp.startTime = LocalDateTime.now();
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
        try{
            pakli.athelyezKartya(pakli.randomKartyIndex(), 2);
        }catch(Exception e){}
        
        //Listenerek beállítása
        kartyaListeners(randomKartyaView);
        for(int i = 0;i<7;i++){
            oszlopListeners(osszOszlop[i], i);
            if(i<4){
                halomListeners(osszHalom[i], i);
            }
        }
        pakliView.setOnMouseClicked(event->{
            if (pakli.csereKratya())
                refreshView();
            else{
                LOGGER.info("Nincs több lap a pakliban.");
                randomKartyaView.setImage(null);
                refreshView();
            }
        });
        
        //kiinduló helyzet beállítása
        ///*
        try{
            int aktIndex;
            for(int i = 0;i<7;i++){
                for(int j = 0;j<i;j++){
                    aktIndex = pakli.randomKartyIndex();
                    pakli.athelyezKartya(aktIndex, i+7);
                    pakli.getKartyak().get(aktIndex).setVisible(false);
                }
                aktIndex = pakli.randomKartyIndex();
                    pakli.athelyezKartya(aktIndex, i+7);
            }
        }catch(Exception e){}
        //*/
       
       //nyertes állapot előidézése
       /*
       List<Kartya> rendezett = pakli.getKartyak().stream()
               .sorted((a1,a2)->a2.getNumValue().compareTo(a1.getNumValue()))
               .collect(Collectors.toList());
       for (int i = 0;i<45;i++){
           rendezett.get(i).setPlaceID(3);
           rendezett.get(i).setStackNumber(13-rendezett.get(i).getNumValue());
           rendezett.get(i).setVisible(true);
           i++;
           rendezett.get(i).setPlaceID(4);
           rendezett.get(i).setStackNumber(13-rendezett.get(i).getNumValue());
           rendezett.get(i).setVisible(true);
           i++;
           rendezett.get(i).setPlaceID(5);
           rendezett.get(i).setStackNumber(13-rendezett.get(i).getNumValue());
           rendezett.get(i).setVisible(true);
           i++;
           rendezett.get(i).setPlaceID(6);
           rendezett.get(i).setStackNumber(13-rendezett.get(i).getNumValue());
           rendezett.get(i).setVisible(true);
           
       }
       */
       pakli.csereKratya();
       refreshView();
    }
    
    /**
     * Beállítja a kártyák működéséhez szükséges Listenereket.
     * @param image A View.nak az aktuális kártyát reprezentáló eleme.
     */
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
                
            LOGGER.info("A(z) {} indexü kártya meg lett fogva.",draggedIngex);
        });
        
        image.setOnDragDone((event)->{
            if(pakli.getKartyak().stream().filter(w->w.getPlaceID().equals(2)).count() == 0){
                try{
                pakli.athelyezKartya(pakli.randomKartyIndex(), 2);
                }catch(Exception e){
                    
                }
            }
            
            event.consume();
            refreshView();
        });
    }
    
    /**
     * Beállítja a oszlopok működéséhez szükséges Listenereket
     * @param oszlop A View, oszlopot reprezentáló tagja.
     * @param i Az oszlop sorszáma.
     */
    private void oszlopListeners(AnchorPane oszlop, int i){
        oszlop.setOnDragEntered((event)->{
            if (pakli.athelyezHitelesites(draggedIngex, i+7))            
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
                pakli.setKovetkezoLathato(draggedIngex);
                List<Kartya> indexek = pakli.getKartyak().stream()
                        .filter(w->w.getPlaceID().equals(pakli.getKartyak().get(draggedIngex).getPlaceID()) && (w.getStackNumber()>=pakli.getKartyak().get(draggedIngex).getStackNumber()))
                        .sorted((a1,a2)-> a1.getStackNumber().compareTo(a2.getStackNumber()))
                        .collect(Collectors.toList());
                
                indexek.forEach(item->{
                    pakli.athelyezKartya(item.getIndex(), i+7);
                });
                
                LOGGER.info("Sikeres áthelyezés ide: {}. oszlop",i);
            }
            
            event.setDropCompleted(true);
            
            event.consume();
        });
        
    }
    
    /**
     * Beállítja a halmok működéséhez szükséges Listenereket.
     * @param halom A View, halmot reprezentáló eleme.
     * @param i A halom sorszáma.
     */
    private void halomListeners(ImageView halom, int i){
        halom.setOnDragEntered((event)->{
           if (pakli.athelyezHitelesites(draggedIngex, i+3))            
                halom.setBlendMode(BlendMode.BLUE);
            else halom.setBlendMode(BlendMode.GREEN);
            event.consume();
            
        });
        halom.setOnDragExited((event)->{
            halom.setBlendMode(null);
            
            event.consume();
        });
        halom.setOnDragOver((event)->{
            if (halom.getBlendMode().equals(BlendMode.BLUE))
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
            
        });
        halom.setOnDragDropped((event)->{
            Dragboard db = event.getDragboard();
            db.clear();
            
            if(halom.getBlendMode().equals(BlendMode.BLUE)){
                    pakli.setKovetkezoLathato(draggedIngex);
                    pakli.athelyezKartya(draggedIngex, i+3);
                    LOGGER.info("Sikeres áthelyezés ide: {}. halom",i);
            }
            
            event.setDropCompleted(true);
            
            event.consume();
            
        });
    }
    
    /**
     * frissíti a megjelenített képet
     */
    private void refreshView(){
        //oszlopok frissítése
        for(int i=0;i<7;i++){
            osszOszlop[i].getChildren().clear();
            //visszaRendezesOszlop(i);
            drawOszlop(i);
            if(i<4){
                drawHalom(i);
            }
            try {
                randomKartyaView.setImage(pakli.getKartyak().stream().filter(w->w.getPlaceID().equals(2)).findFirst().get().getFace());
            } catch (Exception e) {
                randomKartyaView.setImage(null);
            }
        }
        if(pakli.jatekVege()){
            this.jatekosNyert();
        }
        
    }
    
    /**
     * Az oszlopok megjelenítése a feledata.
     * @param i 
     */
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
    
    /**
     * a halmok megjelenítése a feladata
     * @param i a halom sorszáma
     */
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
    /**
     * Ha a játékos meg tudta nyerni a játékot, akkor ez a metódus irányítja át
     * egy másik scene-re.
     */
    private void jatekosNyert()
    {
        FXMLLoader loader;
        Stage stage;
        Parent root = null;
        
        stage = (Stage)randomKartyaView.getScene().getWindow();
        
        loader = new FXMLLoader(getClass().getResource("/fxml/EndScene.fxml"));
        try{
        root=loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
        }catch(Exception e){
            LOGGER.error("Sikertelen Scene váltás");
        }
    }
    
    
    
    
}