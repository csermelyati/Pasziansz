/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproyect.pasziansz.Model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Ez az osztály a játékosok nevét és játékidejét hivatott tárolni
 * @author csermely
 */
public class Scores {
    private StringProperty name;
    private LocalDateTime start;
    private LocalDateTime end;
    private StringProperty minutes;
    
    public Scores(String n, LocalDateTime s, LocalDateTime e){
        this.name.set(n);
        this.start = s;
        this.end = e;
        //calc minutes
    }
    public Scores(String n,String m){
        this.name.set(n);
        this.minutes.set(m);
    }
    public long elteltPercek(){
        return LocalDateTime.from(start).until(end, ChronoUnit.MINUTES);
    }

    public String getName() {
        return name.get();
    }
    public String getMinutes() {
        return minutes.get();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public void setMinutes(String min) {
        this.minutes.set(min);
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    
    public StringProperty nameProperty() { 
         if (name == null) name = new SimpleStringProperty(this, "name");
         return name; 
     }
    public StringProperty minutesProperty() { 
         if (minutes == null) minutes = new SimpleStringProperty(this, "minutes");
         return minutes; 
     }
    
    
    
}
