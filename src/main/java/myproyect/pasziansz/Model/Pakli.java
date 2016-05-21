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
}
