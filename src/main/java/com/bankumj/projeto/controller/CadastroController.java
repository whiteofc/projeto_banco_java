package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.model.Alerta;
import com.bankumj.projeto.model.ValidaCpf;
import com.bankumj.projeto.model.ValidaEmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.mail.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CadastroController implements Initializable {

    @FXML
    private TextField contato;

    @FXML
    private Button confirmaCadastro;

    @FXML
    private TextField cpf;

    @FXML
    private DatePicker dataNascimento;

    @FXML
    private TextField email;

    @FXML
    private TextField endereco;

    @FXML
    private TextField nome;

    @FXML
    private TextField deposito;

    @FXML
    private PasswordField senha;

    @FXML
    private PasswordField repSenha;

    private Connection conexao = ConexaoBD.getConnection();

    Random rand = new Random();
    public static int codigo;
    public static String cpff;

    @FXML
    void cadastrar(ActionEvent event) throws EmailException {

        Alerta alerta = new Alerta();
        ValidaCpf validadorCpf = new ValidaCpf();
        ValidaEmail validadorEmail = new ValidaEmail();

        boolean verificaDigitosDeposito = deposito.getText().matches("^\\d+$");
        boolean verificaDigitosContato = contato.getText().matches("^\\d+$");

        LocalDate dataVencimento = dataNascimento.getValue();

        if (dataVencimento == null) {
            alerta.dataNascimento();

        } else {

            String formataData = dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (contato.getText().isEmpty() || cpf.getText().isEmpty() || email.getText().isEmpty()
                    || endereco.getText().isEmpty() || nome.getText().isEmpty() || deposito.getText().isEmpty() || senha.getText().isEmpty()
                    || repSenha.getText().isEmpty()) {

                alerta.campoVazio();

            } else if (!senha.getText().equalsIgnoreCase(repSenha.getText())) {
                alerta.confereSenha();

            } else if(verificaDigitosDeposito == false){
                alerta.apenasDigitosDeposito();

            } else if(verificaDigitosContato == false){
                alerta.apenasDigitosContato();

            } else if (Double.parseDouble(deposito.getText()) <= 0) {
                alerta.depositoInvalido();

            } else if (senha.getText().length() < 8) {
                alerta.senhaMinima();

            } else if (cpf.getText().length() < 11) {
                alerta.cpfDigitos();

            } else if (ValidaCpf.isCPF(cpf.getText()) == false) {
                alerta.cpfInvalido();

            } else if(ValidaEmail.isValidEmailAddressRegex(email.getText()) == false){
                alerta.emailInvalido();

            } else {

                LocalDate dataAtual = LocalDate.now();
                Period periodo = Period.between(dataVencimento, dataAtual);

                int idade = periodo.getYears();

                if (idade < 18) {
                    alerta.menorIdade();
                } else {

                    String sql = "SELECT * FROM cliente";
                    String cpf_banco = null;
                    String email_banco = null;
                    String telefone_banco = null;

                    boolean aux = true;

                    try {
                        PreparedStatement statement = conexao.prepareStatement(sql);
                        ResultSet rs = statement.executeQuery();

                        while (rs.next()) {
                            cpf_banco = rs.getString("cpf");
                            email_banco = rs.getString("email");
                            telefone_banco = rs.getString("contato");

                            if (cpf.getText().equals(cpf_banco)) {
                                alerta.cpfExistente();
                                aux = false;
                                break;
                            } else if (email.getText().equals(email_banco)) {
                                alerta.emailExistente();
                                aux = false;
                                break;
                            } else if (contato.getText().equals(telefone_banco)) {
                                alerta.contatoExistente();
                                aux = false;
                                break;
                            }
                        }

                        if (aux == true) {
                            String meuEmail = "email@live.com";
                            String minhaSenha = "*******";

                            Properties p = new Properties();

                            p.put("mail.smtp.host", "smtp.office365.com");
                            p.setProperty("mail.smtp.starttls.enable", "true");
                            p.put("mail.smtp.ssl.trust", "smtp.office365.com");
                            p.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
                            p.setProperty("mail.smtp.port", "587");
                            p.setProperty("mail.smtp.user", meuEmail);
                            p.setProperty("mail.smtp.auth", "true");

                            Session s = Session.getDefaultInstance(p);
                            MimeMessage msg = new MimeMessage(s);

                            msg.setFrom(new InternetAddress(meuEmail));
                            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getText()));
                            msg.setSubject("CONFIRMAÇÃO DE CADASTRO - BANCO UMJ");
                            msg.setText("Olá " + nome.getText() + ", seja muito bem-vindo!\n\nEstamos muito felizes que tenha nos escolhido\n\nAgora, para concluir seu cadastro, insira " +
                                    "o código abaixo no campo de confirmação\n\nCódigo: " + codigo + "\n\nAtt: Banco UMJ - O Banco de todos nós.");

                            Transport t = s.getTransport("smtp");
                            t.connect(meuEmail, minhaSenha);
                            t.sendMessage(msg, msg.getAllRecipients());
                            t.close();

                            Alert enviado = new Alert(Alert.AlertType.CONFIRMATION);

                            enviado.setTitle("Cadastro");
                            enviado.setHeaderText("Cadastro efetuado");
                            enviado.setContentText("Verifique seu e-mail e informe o código para confirmação da conta");
                            enviado.showAndWait();

                            String cliente = "INSERT INTO cliente (nome, cpf, endereco, email, data_nascimento, contato, senha) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            String conta = "INSERT INTO conta (cpf_cliente, agencia, saldo) VALUES (?, ?, ?)";

                            try {

                                PreparedStatement stm = conexao.prepareStatement(cliente);
                                PreparedStatement std = conexao.prepareStatement(conta);

                                stm.setString(1, nome.getText());
                                stm.setString(2, cpf.getText());
                                stm.setString(3, endereco.getText());
                                stm.setString(4, email.getText());
                                stm.setString(5, formataData);
                                stm.setString(6, contato.getText());
                                stm.setString(7, senha.getText());
                                stm.execute();
                                stm.close();

                                cpff = cpf.getText();

                                std.setString(1, cpf.getText());
                                std.setInt(2, 1542);
                                std.setDouble(3, Double.parseDouble(deposito.getText()));
                                std.execute();
                                std.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/bankumj/projeto/confirma.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            Stage stage = new Stage();
                            stage.setTitle("Cadastro");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.show();

                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    } catch (AddressException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codigo = rand.nextInt(10000) + 1;
    }
}
