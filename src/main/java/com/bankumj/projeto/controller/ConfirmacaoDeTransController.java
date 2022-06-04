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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class ConfirmacaoDeTransController implements Initializable {

    public static DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");


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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/transferencia.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage2 = new Stage();
            stage2.setTitle("Confirmação");
            stage2.setScene(scene);
            stage2.setResizable(false);
            stage2.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void confirmarTransacao(ActionEvent event) {
        MovimentacoesController css = new MovimentacoesController();
        String sqlSelect = "SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where numero_conta = (?)";
        String sqlUpdate ="UPDATE conta SET saldo = (?)+(?) WHERE numero_conta = (?)";
        String sqlRemocaoValor = "UPDATE conta SET saldo = (?)-(?) WHERE numero_conta = (?)";
        String sqlSaldo ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cliente.cpf = (?)";

        String insertMov = "insert into movimentacoes(data_transacao,num_conta, valor, nome_destinatario)values(?,?,?,?)";

        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sqlUpdate);
            PreparedStatement stm2 = conexao.prepareStatement(sqlSelect);
            stm2.setString(1, Integer.toString(TransferenciaController.contaAux));
            PreparedStatement stm3 = conexao.prepareStatement(sqlRemocaoValor);
            PreparedStatement stm4 = conexao.prepareStatement(sqlSaldo);
            stm4.setString(1, TelaLogin.cpfAux);
            ResultSet rs2 = stm4.executeQuery();
            ResultSet rs = stm2.executeQuery();
            rs.next();
            rs2.next();

            stm.setString(1, rs.getString("saldo"));
            stm.setString(2, Double.toString(TransferenciaController.valorTranAux));
            stm.setString(3, Integer.toString(TransferenciaController.contaAux));
            stm.execute();

            stm3.setString(1, rs2.getString("saldo"));
            stm3.setString(2, Double.toString(TransferenciaController.valorTranAux));
            stm3.setString(3, rs2.getString("numero_conta"));
            stm3.execute();

            PreparedStatement stmInsert = conexao.prepareStatement(insertMov);
            stmInsert.setString(1, formatoData.format(LocalDateTime.now()));
            stmInsert.setString(2, rs2.getString("numero_conta"));
            stmInsert.setString(3, Double.toString(-TransferenciaController.valorTranAux));
            stmInsert.setString(4, rs.getString("nome"));
            stmInsert.execute();



            PreparedStatement stmInsert2 = conexao.prepareStatement(insertMov);
            stmInsert2.setString(1, formatoData.format(LocalDateTime.now()));
            stmInsert2.setString(2, rs.getString("numero_conta"));
            stmInsert2.setString(3, Double.toString(TransferenciaController.valorTranAux));
            stmInsert2.setString(4, rs2.getString("nome"));
            stmInsert2.execute();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setContentText("Transação efetuada com sucesso\n");
            alerta.showAndWait();

            String sql ="SELECT saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?)";
            Connection conexao2 = ConexaoBD.getConnection();
            try {
                PreparedStatement stmRefresh = conexao2.prepareStatement(sql);
                stmRefresh.setString(1, TelaLogin.cpfAux);
                ResultSet rsRefresh = stmRefresh.executeQuery();
                rsRefresh.next();
                MenuController.saldo_banco = rsRefresh.getDouble("saldo");

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

        nomeTitularConfir.setText(Selects.consultaDadosTrans(TransferenciaController.contaAux, TransferenciaController.agenciaAux, "nome"));
        numAgenciaConfir.setText(Integer.toString(TransferenciaController.agenciaAux));
        numContaConfir.setText(Integer.toString(TransferenciaController.contaAux));
        valorDaTranferencia.setText(String.format("%.2f", TransferenciaController.valorTranAux));
        saldoFim.setText(String.format("%.2f",TransferenciaController.saldoFimAux));
    }
}