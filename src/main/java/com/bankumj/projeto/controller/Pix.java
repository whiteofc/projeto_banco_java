package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.dao.Selects;
import com.bankumj.projeto.model.ValidaCpf;
import com.bankumj.projeto.model.ValidaEmail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Pix implements Initializable {

    public static boolean verificaConta = true;
    public Label saldo;
    ObservableList list = FXCollections.observableArrayList();

    private String banco = "Nubank";
    private String nome = "Eraldo Mendes";
    private double valor_atual = 0.0;


    @FXML
    private TextField key_input;

    @FXML
    private TextField valor_input;

    @FXML
    private Button confirmar_button;

    @FXML
    private Separator valor_textfield;

    @FXML
    private ChoiceBox<String> keys;

    public static String key_aux;
    public static Double valor_aux;

    public static int contaAux;
    public static int agenciaAux;
    public static double valorTranAux;
    public static double saldoFimAux;


    public void initialize(URL url, ResourceBundle rb){
        String sql ="SELECT saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, TelaLogin.cpfAux);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                saldo.setText(String.format("R$ %.2f", rs.getDouble("saldo")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        loadKeys();
        confirmar_button.setStyle("-fx-background-color: #5fd75f; -fx-background-radius: 15px; -fx-text-fill: #ffffff");
        keys.setStyle("-fx-background-color: #e5e4e2; -fx-text-fill: #ffffff");
    }

    private void loadKeys(){
        list.removeAll(list);
        String key_cpf = "CPF";
        String key_email = "Email";
        list.addAll(key_cpf,key_email);
        keys.getItems().addAll(list);
    }

    @FXML
    void confirmar(ActionEvent event) {
        ValidaCpf validor_cpf = new ValidaCpf();
        ValidaEmail validor_email = new ValidaEmail();
        try {
            valor_aux = Double.parseDouble(valor_input.getText());
            key_aux = key_input.getText();

            String selectedChoice = keys.getSelectionModel().getSelectedItem();
            if(key_aux.equals(TelaLogin.cpfAux) || key_aux.equals(TelaLogin.emailAux)){
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setContentText("Você nao pode fazer uma transfêrencia para si mesmo!\n");
                alerta.showAndWait();
            }
            else {
                if(valor_aux <= 0){
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setContentText("Valor não pode ser negativo!\n");
                    alerta.showAndWait();
                }
                else if(valor_aux > MenuController.saldo_banco){
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setContentText("Saldo insuficiente!\n");
                    alerta.showAndWait();
                }
                else {
                    if (selectedChoice.equals("CPF")) {
                        if (ValidaCpf.isCPF(key_input.getText()) == true) {
                            key_input.getText();

                            String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";
                            Connection conexao = ConexaoBD.getConnection();
                            try {
                                PreparedStatement stm = conexao.prepareStatement(sql);
                                stm.setString(1, TelaLogin.cpfAux);
                                ResultSet rs = stm.executeQuery();
                                rs.next();
                                contaAux = rs.getInt("numero_conta");
                                agenciaAux = rs.getInt("agencia");
                                saldoFimAux = rs.getDouble("saldo") - valor_aux;
                                Selects.consultaContaPix(key_aux);
                                if(verificaConta == true) {
                                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/confirmacaoDePix.fxml"));
                                    Scene scene = null;
                                    try {
                                        scene = new Scene(fxmlLoader.load());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Stage stage = new Stage();
                                    stage.setTitle("Confirmação");
                                    stage.setScene(scene);
                                    stage.setResizable(false);
                                    stage.show();
                                }
                                Stage stage2 = (Stage) confirmar_button.getScene().getWindow();
                                stage2.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setContentText("Cpf inválido!\n");
                            alerta.showAndWait();
                        }
                    } else if (selectedChoice.equals("Email")) {
                        if (validor_email.isValidEmailAddressRegex(key_input.getText()) == true) {
                            key_aux = key_input.getText();

                            String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";
                            Connection conexao = ConexaoBD.getConnection();
                            try {
                                PreparedStatement stm = conexao.prepareStatement(sql);
                                stm.setString(1, TelaLogin.cpfAux);
                                ResultSet rs = stm.executeQuery();
                                rs.next();
                                contaAux = rs.getInt("numero_conta");
                                agenciaAux = rs.getInt("agencia");
                                saldoFimAux = rs.getDouble("saldo") - valor_aux;

                                Selects.consultaContaPix(key_aux);
                                if(verificaConta == true) {
                                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/confirmacaoDePix.fxml"));
                                    Scene scene = null;
                                    try {
                                        scene = new Scene(fxmlLoader.load());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Stage stage = new Stage();
                                    stage.setTitle("Confirmação");
                                    stage.setScene(scene);
                                    stage.setResizable(false);
                                    stage.show();
                                }
                                Stage stage2 = (Stage) confirmar_button.getScene().getWindow();
                                stage2.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setContentText("Email inválido!\n");
                            alerta.showAndWait();
                        }
                    } else {
                        Alert alerta = new Alert(Alert.AlertType.WARNING);
                        alerta.setContentText("Chave não pode ser em branco!\n");
                        alerta.showAndWait();
                    }
                }
            }
        }catch (NumberFormatException nfe){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setContentText("Valor inválido!\n");
            alerta.showAndWait();
        }
    }

}
