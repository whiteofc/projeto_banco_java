package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Label agencia_menu;

    @FXML
    private Label conta_menu;

    @FXML
    private Label nome_menu;

    @FXML
    private Label saldo_menu;

    @FXML
    private Button sair;

    @FXML
    void logoff(ActionEvent event) throws IOException {

        Stage stage = (Stage) sair.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/loginbanco.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage st = new Stage();
        st.setTitle("Empréstimo");
        st.setScene(scene);
        st.setResizable(false);
        st.show();
    }

    public static double saldo_banco;

    public static int numeroContaAux;

    public static int agenciaPrincipal;


    @FXML
    void refresh(ActionEvent event) {
        String sql ="SELECT saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, TelaLogin.cpfAux);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                saldo_menu.setText(String.format("R$ %.2f", rs.getDouble("saldo")));
                saldo_banco = rs.getDouble("saldo");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void telaEmprestimo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/Emprestimo.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Empréstimo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void telaPix(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/pix.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Pix");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void telaTransferencia(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/transferencia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Transferência");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void telaExtrato(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/movimentacoes.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Movimentações");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql ="SELECT nome, numero_conta, agencia, saldo FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, TelaLogin.cpfAux);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                nome_menu.setText(rs.getString("nome"));
                agencia_menu.setText(rs.getString("agencia"));
                agenciaPrincipal = rs.getInt("agencia");
                conta_menu.setText(rs.getString("numero_conta"));
                numeroContaAux = rs.getInt("numero_conta");
                saldo_menu.setText(String.format("R$ %.2f", rs.getDouble("saldo")));
                saldo_banco = rs.getDouble("saldo");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}