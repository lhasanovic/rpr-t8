package sample;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;


public class FormaController implements Initializable {

    @FXML
    private Label labelaIme;
    @FXML
    private TextField ime;
    @FXML
    private Label labelaAdresa;
    @FXML
    private TextField adresa;
    @FXML
    private Label labelaGrad;
    @FXML
    private TextField grad;
    @FXML
    private Label labelaPostanskiBroj;
    @FXML
    private TextField postanskiBroj;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelaIme.setText("Ime i prezime:");
        labelaAdresa.setText("Adresa:");
        labelaGrad.setText("Grad:");
        labelaPostanskiBroj.setText("Poštanski broj");

        postanskiBroj.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean staraVrijednost, Boolean novaVrijednost) {
                if (novaVrijednost) {

                } else {

                    new Thread(() -> {
                        try {
                            pošaljiZahtjev();
                        } catch (Exception ex) {
                            Logger.getLogger(FormaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }).start();
                }
            }
        });
    }

    private void pošaljiZahtjev() throws Exception {

        String url = "http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + postanskiBroj.getText();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Paint boja = "OK".equals(response.toString()) ? Paint.valueOf("yellowgreen") : Paint.valueOf("lightpink");
        postanskiBroj.setBackground(new Background(new BackgroundFill(boja, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
