package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label labela;
    @FXML
    private TextField uzorak;
    @FXML
    private Button dugmeTrazi;
    @FXML
    private Button dugmePrekini;
    @FXML
    private ListView<String> lista;
    private ObservableList<String> fajlovi;
    private Thread thread;

    @FXML
    private void handleDugmeTrazi(ActionEvent event) {
        fajlovi = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.home"));
        File[] listaFajlova = folder.listFiles();

        thread = new Thread (() -> {
            dugmeTrazi.setDisable(true);
            dugmePrekini.setDisable(false);
            rekurzivnaPretraga(listaFajlova, folder);
            dugmeTrazi.setDisable(false);
            dugmePrekini.setDisable(true);
        });

        thread.start();

        lista.setItems(fajlovi);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labela.setText("Uzorak:");
        dugmePrekini.setDisable(true);

        lista.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("FXMLForma.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Slanje datoteke ");
                    stage.setScene(new Scene(root, 400, 150));
                    stage.show();

                }
                catch (IOException e) {
                    // handle grešku
                }
            }
        });

        dugmePrekini.setOnAction((ActionEvent e) -> {
            thread.stop();
            dugmeTrazi.setDisable(false);
            dugmePrekini.setDisable(true);
        });
    }

    public void rekurzivnaPretraga(File[] listaFajlova, File folder) {
        if (listaFajlova != null) {
            for (File fajl : listaFajlova) {

                if (fajl.isFile() && fajl.getName().toLowerCase().contains(uzorak.getText().toLowerCase())) {

                    Platform.runLater(() -> {
                        fajlovi.add(folder.getPath() + File.separator + fajl.getName());
                    });
                } else if (fajl.isDirectory()) {
                    rekurzivnaPretraga(fajl.listFiles(), fajl);
                }
            }
        }
    }
}