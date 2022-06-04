package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.dao.Selects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ConfirmacaoDePix implements Initializable {

    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private Label ConfirmationNome;

    @FXML
    private Label confirmationAgencia;

    @FXML
    private Label confirmationConta;

    @FXML
    private Button fecharTela;

    @FXML
    private Button confirmarTransacaoButton;

    @FXML
    private Label nomeTitularConfir;

    @FXML
    private Label numAgenciaConfir;

    @FXML
    private Label numContaConfir;

    @FXML
    private Label saldoFim;

    @FXML
    private Label valorDaTranferencia;

    @FXML
    void cancelarTransacao(ActionEvent event) {
        Stage stage = (Stage) fecharTela.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/pix.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage2 = new Stage();
            stage2.setTitle("Pix");
            stage2.setScene(scene);
            stage2.setResizable(false);
            stage2.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void confirmarTransacao(ActionEvent event) {
        String sqlSelect = "SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?) or email = (?)";
        String sqlUpdate ="UPDATE conta SET saldo = (?)+(?) WHERE numero_conta = (?)";
        String sqlRemocaoValor = "UPDATE conta SET saldo = (?)-(?) WHERE numero_conta = (?)";
        String sqlSaldo ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";

        String insertMov = "insert into movimentacoes(data_transacao,num_conta, valor, nome_destinatario)values(?,?,?,?)";

        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sqlUpdate);
            PreparedStatement stm2 = conexao.prepareStatement(sqlSelect);
            stm2.setString(1, (Pix.key_aux));
            stm2.setString(2, (Pix.key_aux));
            PreparedStatement stm3 = conexao.prepareStatement(sqlRemocaoValor);
            PreparedStatement stm4 = conexao.prepareStatement(sqlSaldo);
            stm4.setString(1, TelaLogin.cpfAux);
            ResultSet rs = stm2.executeQuery();
            ResultSet rs2 = stm4.executeQuery();

            rs.next();// dados do destinatario
            rs2.next();// dados do remetente




            stm.setString(1, rs.getString("saldo"));
            stm.setString(2, Double.toString(Pix.valor_aux));
            stm.setString(3, rs.getString("numero_conta"));
            stm.execute();

            stm3.setString(1, rs2.getString("saldo"));
            stm3.setString(2, Double.toString(Pix.valor_aux));
            stm3.setString(3, rs2.getString("numero_conta"));
            stm3.execute();

            PreparedStatement stmInsert = conexao.prepareStatement(insertMov);
            stmInsert.setString(1, formatoData.format(LocalDateTime.now()));
            stmInsert.setString(2, rs2.getString("numero_conta"));
            stmInsert.setString(3, Double.toString(-Pix.valor_aux));
            stmInsert.setString(4, rs.getString("nome"));
            stmInsert.execute();

            PreparedStatement stmInsert2 = conexao.prepareStatement(insertMov);
            stmInsert2.setString(1, formatoData.format(LocalDateTime.now()));
            stmInsert2.setString(2, rs.getString("numero_conta"));
            stmInsert2.setString(3, Double.toString(+Pix.valor_aux));
            stmInsert2.setString(4, rs2.getString("nome"));
            stmInsert2.execute();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setContentText("Transação efetuada com sucesso\n");
            alerta.showAndWait();


            String sql ="SELECT saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?)";
            Connection conexao2 = ConexaoBD.getConnection();
            try {
                PreparedStatement stmMenu = conexao2.prepareStatement(sql);
                stmMenu.setString(1, TelaLogin.cpfAux);
                ResultSet rsMenu = stmMenu.executeQuery();
                rsMenu.next();
                MenuController.saldo_banco = rsMenu.getDouble("saldo");

            }catch (Exception e){
                e.printStackTrace();
            }

            Stage stage = (Stage) confirmarTransacaoButton.getScene().getWindow();
            stage.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomeTitularConfir.setText(Selects.consultaDadosPix(Pix.key_aux,"nome"));
        numAgenciaConfir.setText(Integer.toString(Pix.agenciaAux));
        numContaConfir.setText(Integer.toString(Pix.contaAux));
        valorDaTranferencia.setText(String.format("%.2f", Pix.valor_aux));
        saldoFim.setText(String.format("%.2f", Pix.saldoFimAux));
    }
}