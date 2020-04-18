package GUI;

import Classes.MailServer.App;
import Classes.MailServer.User;
import Classes.Misc.AES;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDate;

public class Signup {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField nameField, passwordField, confirmField, addressField;
    @FXML
    private Label invalidNameLabel, invalidAddressLabel, invalidAddressLabel2, invalidAddressLabel3,
            invalidPasswordLabel, invalidPasswordLabel2, invalidConfirmLabel, invalidLabel;
    @FXML
    private ToggleGroup t1;
    @FXML
    private DatePicker datePicker;

    @FXML
    private boolean checkName() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            invalidNameLabel.setText("");
            return false;
        }
        if (Utils.validString(name)) {
            invalidNameLabel.setTextFill(Color.web("#006600"));
            invalidNameLabel.setText("valid name");
            return true;
        }
        invalidNameLabel.setTextFill(Color.web("#ff0000"));
        invalidNameLabel.setText("invalid name");
        return false;

    }

    @FXML
    private boolean checkAddress() {
        String text = addressField.getText().toLowerCase();
        if (text.isEmpty()) {
            invalidAddressLabel.setText("");
            invalidAddressLabel2.setText("");
            invalidAddressLabel3.setText("");
            return false;
        }
        if (User.addressExists(text)) {
            invalidAddressLabel.setTextFill(Color.web("#ff0000"));
            invalidAddressLabel.setText("email address already exists!");
            invalidAddressLabel2.setText("try signing-in instead.");
            invalidAddressLabel3.setText("");
            return false;
        }
        if (Utils.validAddress(addressField.getText())) {
            invalidAddressLabel.setTextFill(Color.web("#006600"));
            invalidAddressLabel.setText("valid address");
            invalidAddressLabel2.setText("");
            invalidAddressLabel3.setText("");
            return true;
        }

        invalidAddressLabel.setTextFill(Color.web("#ff0000"));
        invalidAddressLabel.setText("invalid address");
        invalidAddressLabel2.setText("can only contain numbers, letters, periods or underscore");
        invalidAddressLabel3.setText("make sure to include \"@thetrio.com\"");
        return false;

    }

    @FXML
    private boolean checkPassword() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            invalidPasswordLabel.setText("");
            invalidPasswordLabel2.setText("");
            invalidConfirmLabel.setText("");
            return false;
        }
        if (!Utils.validPassword(password)) {
            invalidPasswordLabel.setText("invalid password");
            invalidPasswordLabel2.setText("must be between 6 and 30 characters with no spaces");
            invalidConfirmLabel.setText("");
            return false;
        }
        //valid password
        invalidPasswordLabel.setText("");
        invalidPasswordLabel2.setText("");
        return checkConfirm();

    }

    @FXML
    private boolean checkConfirm() {
        String password = confirmField.getText();
        if (password.isEmpty()) {
            invalidConfirmLabel.setText("");
            return false;
        }
        if (password.equals(passwordField.getText())) {
            invalidConfirmLabel.setTextFill(Color.web("#006600"));
            invalidConfirmLabel.setText("passwords match");
            return true;
        }
        //password not matching
        invalidConfirmLabel.setTextFill(Color.web("#ff0000"));
        invalidConfirmLabel.setText("passwords are not matching");
        return false;

    }

    @FXML
    private void callSignup() {

        if (!allValid()) {
            invalidLabel.setText("invalid information");
        } else {
            LocalDate ld = datePicker.getValue();
            User user = new User(addressField.getText().toLowerCase(), AES.encrypt(passwordField.getText(), passwordField.getText()));
            user.setName(nameField.getText());
            user.setGender(((RadioButton)t1.getSelectedToggle()).getText());
            user.setBirthday(new Birthday(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear()));
            App app = new App();
            if (!app.signup(user)) Utils.fileNotFound();
            invalidLabel.setText("");
            user = User.loadInfo(user.getAddress());
            //TODO new scene passing user
        }
        passwordField.setText("");
        confirmField.setText("");
        invalidPasswordLabel.setText("");
        invalidPasswordLabel2.setText("");
        invalidConfirmLabel.setText("");
    }

    @FXML
    private void loadSignin() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("signin.fxml"));
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().sizeToScene();
        rootPane.getScene().getWindow().setHeight(437);
    }

    private boolean allValid() {
        return checkName() && checkAddress() && checkPassword() && checkConfirm() && datePicker.getValue() != null;
    }
}