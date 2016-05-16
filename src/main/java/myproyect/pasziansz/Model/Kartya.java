/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproyect.pasziansz.Model;

import javafx.scene.image.Image;

/**
 *
 * @author csermely
 */
public class Kartya {
    
    private Integer placeID, stackNumber;
    private Image face;
    private boolean visible;
    private String value, type;
    
    
    public Kartya(String Name){
        this.visible = false;
        this.face = new Image(Kartya.class.getResourceAsStream("/Cards/"+Name+".png"));
        String[] details = Name.split("_");
        this.value = details[0];
        this.type = details[2];
        
        this.placeID = 1;

        this.stackNumber = 1;
    }
    public Kartya(Kartya k){
        this.visible = k.isVisible();
        this.face = k.getFace();
        
        this.value = k.getValue();
        this.type = k.getType();
        
        this.placeID = k.getPlaceID();

        this.stackNumber = k.getStackNumber();
    }

    public Integer getPlaceID() {
        return placeID;
    }

    public Integer getStackNumber() {
        return stackNumber;
    }

    public Image getFace() {
        return face;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setPlaceID(Integer placeID) {
        this.placeID = placeID;
    }

    public void setStackNumber(Integer stackNumber) {
        this.stackNumber = stackNumber;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    
}
