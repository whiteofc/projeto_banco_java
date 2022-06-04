package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.dao.Selects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TransferenciaController implements Initializable {

    @FXML
    public TextField numAgencia;

    @FXML
    private TextField numConta;

    @FXML
    private Label saldo;

    @FXML
    private TextField valorTrans;

    public static int agenciaAux;

    public static int contaAux;

    public static double saldoFimAux;

    public static double saldoAux;

    public static double valorTranAux;

    public static boolean verificaConta = true;
    @FXML
    private Button efetuar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql ="SELECT saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, TelaLogin.cpfAux);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                saldoAux = rs.getDouble("saldo");
                saldo.setText(String.format("R$ %.2f", rs.getDouble("saldo")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        efetuar.setStyle("-fx-background-color: #5fd75f; -fx-background-radius: 15px; -fx-text-fill: #ffffff");
    }

    public void checkTrans(javafx.event.ActionEvent actionEvent)  {
        try{
            agenciaAux = Integer.parseInt(numAgencia.getText());
            contaAux = Integer.parseInt(numConta.getText());
            valorTranAux = Double.parseDouble(valorTrans.getText());
            saldoFimAux = saldoAux - Double.parseDouble(valorTrans.getText());

            if(contaAux != MenuController.numeroContaAux) {
                if (Double.parseDouble(valorTrans.getText()) > 0 && Double.parseDouble(valorTrans.getText()) <= MenuController.saldo_banco) {
                    if (numAgencia.getText().length() == 4 && numAgencia.getText().charAt(0) != '0') {
                        if (numConta.getText().length() == 6 && numConta.getText().charAt(0) != '0') {
                            Selects.consultaConta(contaAux, agenciaAux);
                            if (verificaConta == true) {
                                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/confirmacaoDeTransacao.fxml"));
                                Scene scene = new Scene(fxmlLoader.load());
                                Stage stage = new Stage();
                                stage.setTitle("Transferência");
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.show();

                                Stage stage2 = (Stage) efetuar.getScene().getWindow();
                                stage2.close();
                            }
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setContentText("A quantidade de digitos da conta está incorreta ou tem 0 a esquerda\n");
                            alerta.showAndWait();
                        }
                    } else {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setContentText("A quantidade de digitos da agencia está incorreta ou tem 0 a esquerda\n");
                        alerta.showAndWait();
                    }
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setContentText("O valor da transação está incorreto!\n");
                    alerta.showAndWait();
                }
            }else{
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Conta invalida!\n");
                alerta.showAndWait();
            }
        }catch (NumberFormatException nfe){
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Um ou mais campos estão vazios ou com caracter inválido\n");
            alerta.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}