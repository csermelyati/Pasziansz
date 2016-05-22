/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproyect.pasziansz.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import myproyect.pasziansz.Model.Scores;

/**
 * Itt kerül lementésre a felhasználó teljesítménye,
 * mentés után pedig láthatja hogy milyen eremények szölettek eddig.
 * @author csermely
 */
public class EndController implements Initializable {

    @FXML
    private TextField becenevBox;
    @FXML
    private Button okButton;
    @FXML
    private TableView table;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<Scores> teamMembers = new ArrayList<Scores>();
        teamMembers.add(new Scores("Ati", "100"));
        
        
        
        TableColumn<Scores,String> nameCol = new TableColumn<Scores,String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Scores,String> minutesCol = new TableColumn<Scores,String>("Minutes");
        minutesCol.setCellValueFactory(new PropertyValueFactory("minutes"));

        table.getColumns().setAll(nameCol, minutesCol);
        
        table.setItems(FXCollections.observableList(teamMembers));
    }    
    
    private void readXML(){
         try {
             File inputFile = new File("input.txt");
             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
             DocumentBuilder builder = factory.newDocumentBuilder();
             Document doc = builder.parse(inputFile);
             doc.getDocumentElement().normalize();
             NodeList nList = doc.getElementsByTagName("player");
            
         } catch (Exception e) {}
    }
    private void writeXML(){
         try {
             
         } catch (Exception e) {}
    }
    
}
