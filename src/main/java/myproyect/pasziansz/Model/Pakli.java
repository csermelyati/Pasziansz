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
import javafx.scene.image.Image;

/**
 *
 * @author csermely
 */
public class Pakli {
    private List<Kartya> kartyak;
    private Random rand;
    private int currentRandom;
    private Image hatlap;
    
    public Pakli() {
        kartyak = new ArrayList<Kartya>();
        rand = new Random();
        
        ResourceBundle nevek = ResourceBundle.getBundle("Cards.Cards");
        for(int i = 1; i<=51;i++){
            String name = nevek.getString(i+"");
            Kartya k = new Kartya(name);
            kartyak.add(k);
        }
        currentRandom = -1;
        hatlap = new Image(Kartya.class.getResourceAsStream("/Cards/card_back.png"));
    }

    public List<Kartya> getKartyak() {
        return kartyak;
    }
    
    public int getCurrentRandom(){
        return this.currentRandom;
    }
    public void setCurrentRandom(int i){
        this.currentRandom = i;
    }
    public Image getHatlap(){
        return hatlap;
    }
}
