/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author FUJITSU
 */
public class Vigenere extends Application {
    
    private final int ASCII_A = 97;
    private final int ALPHABET = 26;
    private final int ASCII_TOTAL = 256;
    
    private TextField inputTxt;
    private File inputFile;
    private TextField key;
    
    private ComboBox modeBox;
    private ComboBox inputBox;
    private ComboBox resModeBox;
    
    private TextArea result;
    
    public static void main(String[] args)
    {
        launch(Vigenere.class, args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        
        BorderPane border = new BorderPane();
        
        HBox hbox = addHBox(stage);
        border.setTop(hbox);
        border.setLeft(addVBox());
        border.setRight(addFlowPane(stage));
        border.setCenter(addCenterPane());
        
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Vigener Cipher");
        stage.show();
    }

    private HBox addHBox(final Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Input File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Vigenere File", "*.vig"),
//                new FileChooser.ExtensionFilter("Text File", "*.txt")
//        );
        
        inputTxt = new TextField();
        inputTxt.setPrefColumnCount(40);
        inputTxt.setPromptText("plaintext here ... ");
        Button browseBtn = new Button("browse");
        browseBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                inputFile = fileChooser.showOpenDialog(stage);
                if (inputFile != null)
                {
                    System.out.println("file ada");
                }
                else
                {
                    System.out.println("file null");
                }
            }
        });
        
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
        
        modeBox = new ComboBox(modeList);
        modeBox.setValue("Standard");
//        creation using built-in method
        inputBox = new ComboBox();
        inputBox.setItems(inputList);
        inputBox.setValue("Text");
        resModeBox = new ComboBox(resModeList);
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

    private FlowPane addFlowPane(final Stage stage) {
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(5);
        flow.setHgap(15);
        flow.setPrefWidth(100);
//        flow.setStyle("-fx-background-color: F5F6CE;");
        
        Button enBtn = new Button("Enkripsi");
        enBtn.setOnAction(new EventHandler<ActionEvent>() {

            private String plain;
            private String kunci;
            private String cipher;
            
            @Override
            public void handle(ActionEvent t) {
                kunci = key.getText();
                
                if(kunci.length() > 25)
                {
                    System.out.println("ERROR. Kunci terlalu panjang!");
                }
                else
                {
//                    menghilangkan spasi yang ada pada kunci
                    kunci = kunci.replaceAll("\\s+", "");
                    if(kunci.equals(""))
                    {
                        kunci = "a";
                    }
                    if(inputBox.getValue().equals("Text"))
                    {
//                        pemrosesan dilakukan dengan huruf kecil
                        plain = inputTxt.getText();
                    }
                    else if(inputBox.getValue().equals("File"))
                    {
                        if(inputFile!=null)
                        {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                                plain = br.readLine();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Vigenere.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException e){}
                        }
                        else
                        {
                            System.out.println("ERROR. File kosong!");
                        }
                    }
                    if (modeBox.getValue().equals("Standard"))
                    {
                        plain = plain.toLowerCase();
                        StringBuilder sb = new StringBuilder();

                        for (int i=0; i<plain.length(); i++)
                        {
                            if(plain.charAt(i) == ' ')
                            {
                                sb.append(' ');
                            }
                            else
                            {
                                int temp = ((int)plain.charAt(i) - ASCII_A + (int)kunci.charAt(i % kunci.length()) - ASCII_A) % ALPHABET;
                                int resInt = temp + ASCII_A;

                                char res = (char)resInt;
                                sb.append(res);
                            }
                        }
                        cipher = sb.toString();
                    }
                    else if (modeBox.getValue().equals("Extended"))
                    {
                        StringBuilder sb = new StringBuilder();

                        for(int i=0; i<plain.length(); i++)
                        {
                            if(plain.charAt(i) == ' ')
                            {
                                sb.append(' ');
                            }
                            else
                            {
                                int resInt = ((int)plain.charAt(i) + (int)kunci.charAt(i % kunci.length())) % ASCII_TOTAL;

                                char res = (char)resInt;
                                sb.append(res);
                            }
                        }
                        cipher = sb.toString();
                    }
                    if(resModeBox.getValue().equals("Normal"))
                    {
//                        do nothing
                    }
                    else if(resModeBox.getValue().equals("No Space"))
                    {
//                        remove space
                        cipher = cipher.replaceAll("\\s+", "");
                    }
                    else if(resModeBox.getValue().equals("Group-5"))
                    {
                        StringBuilder sbsb = new StringBuilder();
                        cipher = cipher.replaceAll("\\s+", "");
                        while (cipher.length() > 5)
                        {
                            sbsb.append(cipher.substring(0, 5));
                            cipher = cipher.substring(5);
                            sbsb.append(" ");
                        }
                        cipher = sbsb.append(cipher.substring(0)).toString();
                    }
                    result.setText(cipher);
                }
            }
        });
        
        Button decBtn = new Button("Dekripsi");
        decBtn.setOnAction(new EventHandler<ActionEvent>() {

            private String plain;
            private String kunci;
            private String cipher;
            
            @Override
            public void handle(ActionEvent t) {
                kunci = key.getText();
                if(kunci.length() > 25)
                {
                    System.out.println("ERROR. Kunci terlalu panjang!");
                }
                else
                {
//                      menghilangkan spasi yang ada pada kunci
                    kunci = kunci.replaceAll("\\s+", "");
                    if(kunci.equals(""))
                    {
                        kunci = "a";
                    }
                    if(inputBox.getValue().equals("Text"))
                    {
//                        pemrosesan dilakukan dengan huruf kecil
                        cipher = inputTxt.getText();
                    }
                    else if(inputBox.getValue().equals("File"))
                    {
                        if(inputFile!=null)
                        {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                                cipher = br.readLine();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Vigenere.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException e){}
                        }
                        else
                        {
                            System.out.println("ERROR. File kosong!");
                        }
                    }
                    if (modeBox.getValue().equals("Standard"))
                    {
                        cipher = cipher.toLowerCase();
                        StringBuilder sb = new StringBuilder();

                        for (int i=0; i<cipher.length(); i++)
                        {
                            if(cipher.charAt(i) == ' ')
                            {
                                sb.append(' ');
                            }
                            else
                            {
                                int temp = (((int)cipher.charAt(i) - ASCII_A) - ((int)kunci.charAt(i % kunci.length()) - ASCII_A)) % ALPHABET;
                                if(temp < 0)
                                {
                                    temp += ALPHABET;
                                }
                                int resInt = temp + ASCII_A;
                                char res = (char)resInt;
                                sb.append(res);
                            }
                        }
                        plain = sb.toString();
                    }
                    else if (modeBox.getValue().equals("Extended"))
                    {
                        StringBuilder sb = new StringBuilder();

                        for(int i=0; i<cipher.length(); i++)
                        {
                            if(cipher.charAt(i) == ' ')
                            {
                                sb.append(' ');
                            }
                            else
                            {
                                int resInt = ((int)cipher.charAt(i) - (int)kunci.charAt(i % kunci.length())) % ASCII_TOTAL;
                                if (resInt<0)
                                {
                                    resInt += ASCII_TOTAL;
                                }
                                char res = (char)resInt;
                                sb.append(res);
                            }
                        }
                        plain = sb.toString();
                    }
                    if(resModeBox.getValue().equals("Normal"))
                    {
//                        do nothing
                    }
                    else if(resModeBox.getValue().equals("No Space"))
                    {
//                        remove space
                        plain = plain.replaceAll("\\s+", "");
                    }
                    else if(resModeBox.getValue().equals("Group-5"))
                    {
                        StringBuilder sbsb = new StringBuilder();
                        plain = plain.replaceAll("\\s+", "");
                        while (plain.length() > 5)
                        {
                            sbsb.append(plain.substring(0, 5));
                            plain = plain.substring(5);
                            sbsb.append(" ");
                        }
                        plain = sbsb.append(plain.substring(0)).toString();
                    }
                    result.setText(plain);
                }
            }
        });
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//                fileChooser.getExtensionFilters().addAll(
//                        new FileChooser.ExtensionFilter("Vigenere File", "*.vig"),
//                        new FileChooser.ExtensionFilter("Text File", "*.txt")
//                );
                fileChooser.setTitle("Save File");
                File file = fileChooser.showSaveDialog(stage);
                if(file!=null)
                {
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(file);
                        fileWriter.write(result.getText());
                    } catch (IOException ex) {
                        Logger.getLogger(Vigenere.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            fileWriter.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Vigenere.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        
        flow.getChildren().addAll(enBtn,decBtn,saveBtn);
        
        return flow;
    }

    private VBox addCenterPane() {
        VBox vbox = new VBox();
        key = new TextField();
        key.setPromptText("key here ... ");
        key.setPrefColumnCount(30);
        result = new TextArea();
        result.setPrefColumnCount(30);
        result.setPrefRowCount(20);
        result.setEditable(false);
        
        vbox.getChildren().addAll(key,result);
        return vbox;
    }
}
