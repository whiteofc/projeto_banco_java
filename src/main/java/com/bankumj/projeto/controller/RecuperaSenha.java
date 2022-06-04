package com.bankumj.projeto.controller;

import com.bankumj.projeto.dao.ConexaoBD;
import com.bankumj.projeto.model.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RecuperaSenha {

    @FXML
    private TextField recuperaSenha;

    @FXML
    private Button rec;

    Connection conexao = ConexaoBD.getConnection();

    @FXML
    void recuperar(ActionEvent event) {
        Alerta alerta = new Alerta();

        String sql = "SELECT * FROM cliente WHERE email = (?)";
        String senha = null, email = null;
        boolean aux = false;

        try {
            PreparedStatement std = conexao.prepareStatement(sql);
            std.setString(1, recuperaSenha.getText());
            ResultSet rs = std.executeQuery();

            if(recuperaSenha.getText().isEmpty()){
                alerta.emailInvalido();

            } else {

                while(rs.next()){
                    senha = rs.getString("senha");
                    email = rs.getString("email");

                    if(recuperaSenha.getText().equals(email)){
                        String meuEmail = "email@live.com";
                        String minhaSenha = "*******";

                        aux = true;

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
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recuperaSenha.getText()));
                        msg.setSubject("CONFIRMAÇÃO DE CADASTRO - BANCO UMJ");
                        msg.setText("Você solicitou a recuperação de senha!\n\nSUA SENHA: " + senha);

                        Transport t = s.getTransport("smtp");
                        t.connect(meuEmail, minhaSenha);
                        t.sendMessage(msg, msg.getAllRecipients());
                        t.close();

                        Alert enviado = new Alert(Alert.AlertType.CONFIRMATION);

                        enviado.setTitle("Recuperação de senha");
                        enviado.setHeaderText("Solicitação de senha");
                        enviado.setContentText("Verifique seu e-mail e informe o código para confirmação da conta");
                        enviado.showAndWait();

                            Stage stage = (Stage) rec.getScene().getWindow();
                            stage.close();

                        }
                    }
                }

                if(aux == false){
                    alerta.verificaEmailBanco();
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }
    }
