package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.model.Movimentacoes;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MovimentacoesController implements Initializable {


    DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dataFormatadaImpresao = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate dataInicialAux = null;
    LocalDate dataFinalAux = null;

    @FXML
    private TableColumn<Movimentacoes, String> colunaData;

    @FXML
    private TableColumn<Movimentacoes, String> colunaDestino;

    @FXML
    private TableColumn<Movimentacoes, String> colunaValor;

    @FXML
    private DatePicker dtFinal;

    @FXML
    private DatePicker dtInicial;

    @FXML
    private TableView<Movimentacoes> tabelaMovimentacoes;

    ObservableList<Movimentacoes> list = FXCollections.observableArrayList();

    @FXML
    void pesquisar(ActionEvent event) {

        list.removeAll(list);
        dataInicialAux = dtInicial.getValue();
        dataFinalAux = dtFinal.getValue();

        String sql2 ="SELECT * FROM cliente INNER JOIN conta ON cliente.cpf = conta.cpf_cliente where cpf = (?)";
        String sql ="SELECT * FROM movimentacoes where data_transacao between (?) and (?) and num_conta = (?)";
        Connection conexao = ConexaoBD.getConnection();
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            PreparedStatement stm2 = conexao.prepareStatement(sql2);
            stm2.setString(1, TelaLogin.cpfAux);
            ResultSet rs2 = stm2.executeQuery();
            rs2.next();
            stm.setString(1, dataInicialAux.format(dataFormatada));
            stm.setString(2, dataFinalAux.format(dataFormatada));
            stm.setString(3, rs2.getString("numero_conta"));
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                LocalDate formatacaoImprecao = LocalDate.parse(rs.getString("data_transacao"));
                list.add(
                        new Movimentacoes(formatacaoImprecao.format(dataFormatadaImpresao),rs.getString("nome_destinatario"),String.format("R$ %.2f",rs.getDouble("valor")))
                );
            }
        }catch (NullPointerException ne){
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Campos em branco ou data com formatação inválida\n");
            alerta.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaDestino.setCellValueFactory(new PropertyValueFactory<>("nomeDestinatario"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valorTransacao"));
        tabelaMovimentacoes.setItems(list);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dtInicial.setValue(LocalDate.now());
        dtFinal.setValue(LocalDate.now());
    }
}