package com.bankumj.projeto.dao;

import com.bankumj.projeto.controller.Main;
import com.bankumj.projeto.controller.Pix;
import com.bankumj.projeto.controller.TransferenciaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Selects {
    public static String consultaDadosTrans(int numConta, int numAgencia, String coluna) {
        String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where numero_conta = "+numConta+" and agencia = "+numAgencia;
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString(coluna);
            }else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String consultaDadosPix(String key_aux, String coluna) {
        String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = "+key_aux+" or email = "+key_aux;
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString(coluna);
            }else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String consultaDadosEmprestimo(int numConta, int numAgencia, String coluna) {
        String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where numero_conta = "+numConta+" and agencia = "+numAgencia;
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString(coluna);
            }else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void consultaConta(int numConta, int numAgencia) {
        String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente  where numero_conta = "+numConta+" and agencia = "+numAgencia;
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if(rs.next() == false){
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Cliente não existe\n");
                alerta.showAndWait();
                TransferenciaController.verificaConta = false;
            }else{
                TransferenciaController.verificaConta = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void consultaContaPix(String usuario) throws IOException {
        String sql ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente  where cpf = (?) or email = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, usuario);
            stm.setString(2, usuario);
            ResultSet rs = stm.executeQuery();
            if(rs.next() == false){
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Cliente não existe\n");
                alerta.showAndWait();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pix.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Pix");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                Pix.verificaConta = false;
            }else{
                Pix.verificaConta = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}