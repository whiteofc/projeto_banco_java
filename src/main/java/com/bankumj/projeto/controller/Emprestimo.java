package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Emprestimo implements Initializable {

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private Button solicitar;

    @FXML
    private Button disponibilidade;

    @FXML
    private ChoiceBox<String> qtd;

    @FXML
    private Label saldo;

    @FXML
    private TextField valor_emprestimo;

    @FXML
    private TextField renda;

    @FXML
    private Label saldo_disponivel;

    public static double valor,parcelas_emprestimo, verifica;
    public static double renda_fixa,valor_disponivel;

    public boolean check = false;

    @FXML
    void verificar_emprestimo(ActionEvent event) {
        String selectedChoice = qtd.getSelectionModel().getSelectedItem();

        try {
            renda_fixa = Double.parseDouble(renda.getText());
            valor = Double.parseDouble(valor_emprestimo.getText());

            valor_disponivel = renda_fixa * 3;

            parcelas_emprestimo = valor / 12;
            verifica = renda_fixa * 0.3;

            if (valor <= valor_disponivel) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setContentText("Empréstimo aprovado com sucesso. Selecione a forma de pagamento ao lado!");
                alerta.showAndWait();
                saldo_disponivel.setText(String.format("%.2f", valor_disponivel));
                qtd.getItems().removeAll(list);
                loadparcelas();
                check = true;
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Desculpe no momento não conseguir aprovar seu empréstimo, tente novamente em 1 mês!");
                alerta.showAndWait();
            }
        }
        catch (NumberFormatException nfe){
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Um ou mais campos estão vazios ou com caracter inválido\n");
            alerta.showAndWait();
        }
    }

    @FXML
    void solicitar(ActionEvent event) throws IOException {
        if(check == true) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/confirmacaoDeEmprestimo.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Confirmação");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Stage stage2 = (Stage) solicitar.getScene().getWindow();
            stage2.close();
        }
        else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("O valor da transação não pode ser em branco!\n");
            alerta.showAndWait();
        }
    }


    private void loadparcelas(){
        list.removeAll(list);
        double[] valores = new double[13];
        for(int i = 1; i <= 12; i++){
            valores[i] = valor / i;
            list.add(i + "x - R$ " + String.format("%.2f",valores[i]));
        }
        qtd.getItems().addAll(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        solicitar.setStyle("-fx-background-color: #5fd75f; -fx-background-radius: 15px; -fx-text-fill: #ffffff");
    }
}