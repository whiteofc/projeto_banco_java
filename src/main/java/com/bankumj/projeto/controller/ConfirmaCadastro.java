package com.bankumj.projeto.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.model.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfirmaCadastro {

    @FXML
    private TextField codigo;

    @FXML
    private Button confirmar;

    private Connection conexao = ConexaoBD.getConnection();

    @FXML
    void confirmarCadastro(ActionEvent event) {

        Alerta a = new Alerta();

        if(codigo.getText().equals("")){
            a.campoVazio();

        } else if(Integer.parseInt(codigo.getText()) == CadastroController.codigo){
            a.cadastrado();

            Stage stage = (Stage) confirmar.getScene().getWindow();
            stage.close();

        } else if(Integer.parseInt(codigo.getText()) != CadastroController.codigo){
            a.codigoInvalido();
        }
    }
}