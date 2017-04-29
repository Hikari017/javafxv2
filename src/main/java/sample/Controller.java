package sample ;

import com.sun.deploy.util.FXLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.MySqlConnector;
import sample.Utils;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextField loginText;

    @FXML
    PasswordField passwordText;

    @FXML
    TextField registrationLogin;

    @FXML
    TextField lastName;

    @FXML
    TextField phoneNumber;

    @FXML
    PasswordField registrationPassword;

    @FXML
    PasswordField registrationRetypePassword;

//    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("AkademiaKodu");
//        alert.setHeaderText(null);
//        alert.setContentText("Hello, jestem tekstem!");
//
//    ButtonType buttonCancel = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
//    ButtonType buttoOskarIsOk = new ButtonType("Oskar jest fajny!");
//    ButtonType buttonOK = new ButtonType("OK");
//
//        alert.getButtonTypes().setAll(buttonCancel, buttoOskarIsOk, buttonOK);
//
//    Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == buttoOskarIsOk) {
//        System.out.println("Ktoś myśli, że Oskar jest fajny!");
//    } else if (result.get() == buttonCancel) {
//        System.out.println("Ktoś wyłączył okno (anulował)");
//    }

    private boolean isLoginValid() {
        if (loginText.getText().trim().length() < 4 || passwordText.getText().trim().length() < 4) {
            sample.Utils.openDialog("Logowanie", "Login i hasło muszą mieć minimum 4 znaki");
            return false;
        }
        return true;
    }//Qwertyqwerty1!

    public void openDialog(MouseEvent event) throws IOException {
        if (!isLoginValid()) {
            return;
        }
        System.out.println("Login: " + loginText.getText() + " Hasło: " + (Utils.hashPassword(passwordText.getText())));

        Statement statement = MySqlConnector.getInstance().getNewStatement();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE name = '" + loginText.getText()+"' LIMIT 1");

            int counter = 0;

            while (resultSet.next()) {
                String passwordFromDatabase = resultSet.getString("password");
                if (passwordFromDatabase.equals(Utils.hashPassword(passwordText.getText()))) {


                        Parent MainPage = FXMLLoader.load(getClass().getResource("logo.fxml"));
                    Scene scene = new Scene(MainPage);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.hide();
                    stage.setScene(scene);
                    stage.show();
                    Utils.openDialog("Logowanie", "Zalogowałeś się poprawnie");
                } else {
                   Utils.openDialog("Logowanie", "Błędne hasło");
                }
                counter++;
            }
            if (counter == 0) {
               Utils.openDialog("Logowanie", "Użytkownik nie istnieje");
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void signUp() {
        System.out.println("Login: " + registrationLogin.getText() + ", nazwisko: " + lastName.getText() + ", hasło: " +
                registrationPassword.getText() + ", powtórzone hasło: " + registrationRetypePassword.getText() +
                ", nr tel: " + phoneNumber.getText());

        Statement statement = MySqlConnector.getInstance().getNewStatement();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE name = '" + registrationLogin.getText() + "' LIMIT 1");

            if (resultSet.next()) {
                Utils.openDialog("Rejestracja", "Użytkownik o takim loginie już istnieje!");
            } else {
                if (registrationPassword.getText().equals(registrationRetypePassword.getText())) {
                    PreparedStatement signUpStatement = MySqlConnector.getInstance().getConnection().prepareStatement("INSERT INTO user " +
                            "(name, lastName, password, number) VALUES (?, ?, ?, ?)");
                    signUpStatement.setString(1, registrationLogin.getText());
                    signUpStatement.setString(2, lastName.getText());
                    signUpStatement.setString(3, registrationPassword.getText());
                    signUpStatement.setString(4, phoneNumber.getText());

                    signUpStatement.execute();

                    signUpStatement.close();

                    Utils.openDialog("Logowanie", "Dodałem nowego użytkonika " + registrationLogin.getText().toUpperCase());
                } else {
                    Utils.openDialog("Logowanie", "Hasła nie są identyczne!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}