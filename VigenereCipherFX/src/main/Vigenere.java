/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author FUJITSU
 */
public class Vigenere extends Application {
    public static void main(String[] args)
    {
        launch(Vigenere.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();
        
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Vigener Cipher");
        stage.show();
    }

    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        
        TextField inputTxt = new TextField();
        inputTxt.setPrefColumnCount(20);
        Button browseBtn = new Button("browse");
        hbox.getChildren().addAll(inputTxt,browseBtn);
        return hbox;
    }

    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 12, 15, 12));
        vbox.setSpacing(7);
        Text title = new Text("Option");
        title.setFont(Font.font("Arial", FontWeight.BOLD,14));
        vbox.getChildren().add(title);
        
//        creation using constructor
        ObservableList<String> modeList = FXCollections.observableArrayList("Standard","Extended");
        ObservableList<String> inputList = FXCollections.observableArrayList("File","Text");
        ObservableList<String> resModeList = FXCollections.observableArrayList("Normal","No Space","Group-5");
        
        ComboBox modeBox = new ComboBox(modeList);
        modeBox.setValue("Standard");
//        creation using built-in method
        ComboBox inputBox = new ComboBox();
        inputBox.setItems(inputList);
        inputBox.setValue("Text");
        ComboBox resModeBox = new ComboBox(resModeList);
        resModeBox.setValue("Normal");
        
        ComboBox optionBox[] = new ComboBox[]
        {
            modeBox, inputBox, resModeBox
        };
        
        for(int i=0; i<3; i++)
        {
            VBox.setMargin(optionBox[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(optionBox[i]);
        }
        
        return vbox;
    }
}
