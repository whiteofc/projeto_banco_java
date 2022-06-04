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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ConfirmacaoDeEmprestimo implements Initializable {

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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/Emprestimo.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage2 = new Stage();
            stage2.setTitle("Emprestimo");
            stage2.setScene(scene);
            stage2.setResizable(false);
            stage2.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void confirmarTransacao(ActionEvent event) {
        String sql ="UPDATE conta SET saldo = (?) WHERE cpf_cliente = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setDouble(1,MenuController.saldo_banco + Emprestimo.valor);
            stm.setString(2, TelaLogin.cpfAux);
            stm.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        String insertMov = "insert into movimentacoes(data_transacao,num_conta, valor, nome_destinatario)values(?,?,?,'Emprestimo')";
        try {
            PreparedStatement stmInsert = conexao.prepareStatement(insertMov);
            stmInsert.setString(1, ConfirmacaoDeTransController.formatoData.format(LocalDateTime.now()));
            stmInsert.setString(2, Integer.toString(MenuController.numeroContaAux));
            stmInsert.setString(3, Double.toString(Emprestimo.valor));
            stmInsert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) confirmarTransacaoButton.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nomeTitularConfir.setText(Selects.consultaDadosPix(TelaLogin.cpfAux,"nome"));
        numAgenciaConfir.setText(Selects.consultaDadosEmprestimo(MenuController.numeroContaAux,MenuController.agenciaPrincipal,"agencia"));
        numContaConfir.setText(Selects.consultaDadosEmprestimo(MenuController.numeroContaAux, MenuController.agenciaPrincipal,"numero_conta"));
        valorDaTranferencia.setText(String.format("%.2f", Emprestimo.valor));
        saldoFim.setText(String.format("%.2f", Emprestimo.valor + MenuController.saldo_banco));
    }
}