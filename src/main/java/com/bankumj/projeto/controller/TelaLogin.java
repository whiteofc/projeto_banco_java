package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLogin {

    @FXML
    private Button log;

    @FXML
    private PasswordField senha;

    @FXML
    private TextField user;

    private

    @FXML
    void chamatela(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/recuperasenha.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Cadastro");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void criarconta(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/cadastro.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Cadastro");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public  static String cpfAux;
    public  static String emailAux;

    public static String usuarioAux;
    public static String senhaAux;
    private  Connection conexao = ConexaoBD.getConnection();
    @FXML
    void  entra(ActionEvent event) {

        if (user.getText().isEmpty() || senha.getText().isEmpty()) {
            Alert aviso = new Alert(Alert.AlertType.ERROR);
            aviso.setHeaderText("Campo Vazio!");
            aviso.setContentText("Erro ao entrar! Um dos campos está vazio");
            aviso.showAndWait();
        } else {

            String login = "SELECT * FROM cliente WHERE email = (?) OR cpf = (?)";
            String usuario = "";
            String usuarioB = "";
            String pass = "";


            try {
                PreparedStatement stm = conexao.prepareStatement(login);
                stm.setString(1, user.getText());
                stm.setString(2, user.getText());
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    usuario = rs.getString("cpf");
                    usuarioB = rs.getString("email");
                    pass = rs.getString("senha");

                }

                if (user.getText().equalsIgnoreCase(usuario) && senha.getText().equalsIgnoreCase(pass)) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Login");
                    a.setContentText("Bem-vindo!");
                    a.showAndWait();
                    cpfAux = usuario;
                    emailAux = usuarioB;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/menu.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Menu");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    Stage stage1 = (Stage) log.getScene().getWindow();
                    stage1.close();
                }
                else if (user.getText().equalsIgnoreCase(usuarioB) && senha.getText().equalsIgnoreCase(pass)) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Login");
                    a.setContentText("Bem-vindo!");
                    a.showAndWait();
                    cpfAux = usuario;
                    emailAux = usuarioB;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/menu.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Menu");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    Stage stage1 = (Stage) log.getScene().getWindow();
                    stage1.close();
                }
                else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Login");
                    a.setContentText("Usuário ou senha incorretos!");
                    a.showAndWait();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

        }
    }

}