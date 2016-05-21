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
    private int index;
    private Integer placeID, stackNumber;
    private Image face;
    private boolean visible;
    private String value, type;
    private Integer numValue;
    
    
    public Kartya(String Name, int i){
        System.out.println(Name+", "+i);
        this.index = i-1;
        this.visible = false;
        this.face = new Image(Kartya.class.getResourceAsStream("/Cards/"+Name+".png"));
        String[] details = Name.split("_");
        this.value = details[0];
        this.type = details[2];
        
        this.placeID = 1;

        this.stackNumber = 0;
        
        try{
            this.numValue = Integer.parseInt(value);
        }catch(Exception e){
            switch(value){
                case "ace": 
                        this.numValue = 1;
                    break;
                case "jack": 
                        this.numValue = 11;
                    break;
                case "king": 
                        this.numValue = 13;
                    break;
                case "queen": 
                        this.numValue = 12;
                    break;
                    
            }
        }
            
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
    
    public int getIndex(){
        return this.index;
    }

    public Integer getStackNumber() {
        return stackNumber;
    }
    public Integer getNumValue() {
        return numValue;
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
