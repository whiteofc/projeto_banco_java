package com.bankumj.projeto.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Alerta {

    public void emailInvalido(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("O e-mail informado não é válido.");
        a.showAndWait();
    }

    public void campoVazio(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Um dos campos está vazio.");
        a.showAndWait();
    }

    public void confereSenha(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("As senhas não conferem. Por favor, insira novamente.");
        a.showAndWait();
    }

    public void depositoInvalido(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Valor para depósito inválido. Por favor, insira um valor maior que 0.");
        a.showAndWait();
    }

    public void senhaMinima(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("A senha deve ter no minímo 8 caractéres.");
        a.showAndWait();
    }

    public void cpfDigitos(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("CPF deve conter 11 digítos.");
        a.showAndWait();
    }

    public void dataNascimento(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Insira sua data de nascimento.");
        a.showAndWait();
    }

    public void menorIdade(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Apenas maiores de 18 anos podem abrir uma conta");
        a.showAndWait();
    }

    public void cadastrado(){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Cadastro");
        a.setContentText("Cliente cadastrado com sucesso");
        a.setHeaderText("Cadastro concluído");
        a.showAndWait();
    }

    public void erroCadastro(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Houve um erro ao cadastrar cliente no banco");
        a.showAndWait();
    }

    public void codigoInvalido(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Código inválido");
        a.showAndWait();
    }

    public void cpfInvalido(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("O CPF informado é inválido. Por favor, insira um CPF válido");
        a.showAndWait();
    }

    public void cpfExistente(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("O CPF informado já consta em nosso sistema.");
        a.showAndWait();
    }

    public void emailExistente(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("O e-mail informado já consta em nosso sistema.");
        a.showAndWait();
    }

    public void contatoExistente(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("O contato informado já consta em nosso sistema.");
        a.showAndWait();
    }

    public void apenasDigitosDeposito(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Insira apenas números para depósitos.");
        a.showAndWait();
    }

    public void apenasDigitosContato(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Cadastro");
        a.setHeaderText("Falha no cadastro");
        a.setContentText("Insira apenas números para depósitos.");
        a.showAndWait();
    }

    public void verificaEmailBanco(){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Recuperação de senha");
        a.setHeaderText("Falha na recuperação");
        a.setContentText("O e-mail informado não consta em nosso sistema.");
        a.showAndWait();
    }

}
