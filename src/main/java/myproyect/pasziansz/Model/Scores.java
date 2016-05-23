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
 * Ez az osztály a játékosok nevét és játékidejét hivatott tárolni.
 * @author csermely
 */
public class Scores {
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String minutes;
    //CHECKSTYLE:OFF
    public Scores(String n, LocalDateTime s, LocalDateTime e){
        this.name = n;
        this.start = s;
        this.end = e;
        this.minutes = ""+elteltPercek(s,e);
        //calc minutes
    }
    public Scores(String n,String m){
        this.name = n;
        this.minutes = m;
    }
    //CHECKSTYLE:ON
    /**
     * Ez a metódus adja meg, hogy hány perc telt el a játék kezdete és vége közt.
     * @param s Játék kezdeti időpontja.
     * @param e Játék végének időpontja.
     * @return Akezdet és vég közt eltelt idő percekben.
     */
    public Integer elteltPercek(LocalDateTime s, LocalDateTime e){
        return (int)LocalDateTime.from(s).until(e, ChronoUnit.MINUTES);
    }
//CHECKSTYLE:OFF
    public String getName() {
        return name;
    }
    public String getMinutes() {
        return minutes;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setMinutes(String min) {
        this.minutes = min;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    
    
}
