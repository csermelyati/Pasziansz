/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproyect.pasziansz.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

/**
 *
 * @author csermely
 */
public class Pakli {
    private List<Kartya> kartyak;
    private Image hatlap;
    
    public Pakli() {
        kartyak = new ArrayList<Kartya>();
        
        ResourceBundle nevek = ResourceBundle.getBundle("Cards.Cards");
        for(int i = 1; i<=52;i++){
            String name = nevek.getString(i+"");
            Kartya k = new Kartya(name,i);
            kartyak.add(k);
        }
        hatlap = new Image(Kartya.class.getResourceAsStream("/Cards/card_back.png"));
    }

    public List<Kartya> getKartyak() {
        return kartyak;
    }
    
    public Image getHatlap(){
        return hatlap;
    }
    
    /**
     * Eldönti, hogy a kiválasztott kártyát át lehet e helyezni a kívánt helyre
     * 
     * 
     * @param index a kártya indexe a pakliban
     * @param hely a cél helyét jelölő index
     * @return jelzi, hogy az áthelyezés sikeres volt e
     */
    public boolean athelyezHitelesites(int index, int hely){
        Kartya akt = this.getKartyak().get(index);
        try{
            Kartya celMax = this.getKartyak().stream()
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
                Integer max = this.getKartyak().stream()
                        .filter(w->w.getPlaceID().equals(akt.getPlaceID()))
                        .max((a1,a2)->a1.getStackNumber().compareTo(a2.getStackNumber())).get().getStackNumber();
                return celMax.getNumValue().equals(akt.getNumValue()+1) && celMax.getType().equals(akt.getType()) && akt.getStackNumber().equals(max);
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
    public boolean athelyezKartya(int index, int hely){
        try{
            Kartya akt = this.getKartyak().get(index);
            int stackNum;
            try{
                if(hely>2)
                    stackNum = this.getKartyak().stream()
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
     * @return Ha a csere sikeres volt, akkor igaz, ha nem akkor hamis
     */
    public boolean csereKratya(){
        try{
            Kartya akt = this.getKartyak().stream()
                    .filter(w->w.getPlaceID().equals(2))
                    .findFirst().get();
            akt.setPlaceID(1);
            akt.setStackNumber(0);
            akt.setVisible(false);
        }catch(Exception e){}
        
        try{
            this.athelyezKartya(this.randomKartyIndex(), 2);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    /**
     * kiválaszt egy kártyát ami még a pakliban van
     * @return a kiválasztott kártya indexe
     */
    public int randomKartyIndex(){
        List<Kartya> maradek = this.getKartyak().stream()
                .filter(w->w.getPlaceID().equals(1))
                .collect(Collectors.toList());
        Random r = new Random();
        int i;
        do{
            i = r.nextInt(maradek.size());
        }while (!maradek.get(i).getPlaceID().equals(1));
        
        return maradek.get(i).getIndex();
    }
    
    /**
     * Megakadályozza, hogy az oszlopban lévő kártyák elcsússzanak
     * @param i az oszlop sorszáma
     */
    public void visszaRendezesOszlop(int i){
        try{
            
            List<Kartya> lista= this.getKartyak().stream()
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
    /**
     * Ha az oszlopban található kártya az aktuálisan mozgatotton kívül
     * akkor azok közül a legfelsőt láthatóvá teszi
     * @param index a mozgatott kártya indexe
     * @return ha valamiért nem tudja végrehajtani, akkor hamis
     */
    public boolean setKovetkezoLathato(int index){
        try{
            Kartya kovetkezo = this.getKartyak().stream()
                    .filter(w->w.getPlaceID().equals(this.getKartyak().get(index).getPlaceID()) && w.getStackNumber().equals(this.getKartyak().get(index).getStackNumber()-1))
                    .findFirst().get();
            boolean eredeti = kovetkezo.isVisible();
            kovetkezo.setVisible(true);
            return eredeti;
        }catch(Exception e){return true;}
    }
    
    /**
     * Eldönti, hogy a játék valóban véget ért e
     * 
     * @return igaz, ha a játék véget ért, hamis ha még nem.
     */
    public boolean jatekVege(){
        for(Kartya item : this.getKartyak()){
            if (item.getPlaceID() > 6 || item.getPlaceID() < 3)
                return false;
        }
        return true;
    }
}
