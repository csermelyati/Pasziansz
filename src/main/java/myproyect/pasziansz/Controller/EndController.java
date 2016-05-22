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
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import myproyect.pasziansz.MainApp;
import myproyect.pasziansz.Model.Scores;
import org.xml.sax.SAXException;

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
    private TableView<Scores> table;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainApp.endTime = LocalDateTime.now();
        TableColumn<Scores,String> nameCol = new TableColumn<Scores,String>("Name");
        nameCol.setPrefWidth(170);
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Scores,String> minutesCol = new TableColumn<Scores,String>("Minutes");
        minutesCol.setPrefWidth(168);
        minutesCol.setCellValueFactory(new PropertyValueFactory("minutes"));

        table.getColumns().addAll(nameCol, minutesCol);
        
        okButton.setOnAction(event->{
            table.setItems(readXML());
            Scores s = new Scores(becenevBox.getText(), MainApp.startTime, MainApp.endTime);
            table.getItems().add(s);
            table.getItems().sort((a1,a2)-> a1.getMinutes().compareTo(a2.getMinutes()));
            writeXML(table.getItems());
            okButton.setDisable(true);
            becenevBox.setDisable(true);
        });
        
        
    }    
    
    private ObservableList<Scores> readXML(){
        ObservableList<Scores> returnList;
             returnList = FXCollections.observableArrayList();
             
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(EndController.class.getName()).log(Level.SEVERE, null, ex);
            }
                Document doc = null;
            
            try {
                doc = dBuilder.parse(new File(System.getProperty("user.home")+"\\.highscore.xml"));
            } catch (SAXException ex) {}
            catch (IOException ex) {return returnList;}
            
            doc.getDocumentElement().normalize();
             NodeList nList = doc.getElementsByTagName("player");
             for (int i = 0 ; i < nList.getLength() ; i++){
                 Node akt = nList.item(i);
                 if (akt.getNodeType() == Node.ELEMENT_NODE) {
                     Element element = (Element) akt;
                     returnList.add(new Scores(element.getElementsByTagName("name").item(0).getTextContent(),
                                                element.getElementsByTagName("time").item(0).getTextContent()));
                 }
             }
            
             return FXCollections.observableArrayList(returnList.sorted((a1,a2)-> a1.getMinutes().compareTo(a2.getMinutes())));
    }
    private void writeXML(ObservableList<Scores> lista){
         try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("highscore");
		doc.appendChild(rootElement);
                
                for(Scores akt : lista){
                    Element player = doc.createElement("player");
                    rootElement.appendChild(player);
                    
                    Element name = doc.createElement("name");
                    name.appendChild(doc.createTextNode(akt.getName()));
                    player.appendChild(name);
                    
                    Element time = doc.createElement("time");
                    time.appendChild(doc.createTextNode(akt.getMinutes()));
                    player.appendChild(time);
                }

                
                
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = null;
                File outFile = new File(System.getProperty("user.home")+"\\.highscore.xml");
                if(!outFile.exists()){
                    outFile.createNewFile();
                }
                
                result = new StreamResult(outFile);
             
		transformer.transform(source, result);


	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
          catch (IOException ioe) {
		ioe.printStackTrace();
	  }
    }
    
}
