import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TelaLogin extends JFrame implements ActionListener {
    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JButton botaoRegistrar;

    public TelaLogin() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2));

        painel.add(new JLabel("Login:"));
        campoLogin = new JTextField();
        painel.add(campoLogin);

        painel.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painel.add(campoSenha);

        botaoLogin = new JButton("Login");
        botaoLogin.addActionListener(this);
        painel.add(botaoLogin);

        botaoRegistrar = new JButton("Registrar");
        botaoRegistrar.addActionListener(this);
        painel.add(botaoRegistrar);

        add(painel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoLogin) {
            login();
        } else if (e.getSource() == botaoRegistrar) {
            abrirTelaRegistro();
        }
    }

    private void login() {
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            Connection conexao = ConexaoBD.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Usuarios WHERE login = '" + login + "' AND senha = '" + senha + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                abrirTelaUsuario(); // Abre a tela do usuário
            } else {
                rs = stmt.executeQuery("SELECT * FROM Atendentes WHERE login = '" + login + "' AND senha = '" + senha + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                    abrirTelaAtendente(); // Abre a tela do atendente
                } else {
                    JOptionPane.showMessageDialog(this, "Login ou senha incorretos!");
                }
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
        }
    }

    private void abrirTelaRegistro() {
        SwingUtilities.invokeLater(() -> {
            TelaRegistro telaRegistro = new TelaRegistro();
            telaRegistro.setVisible(true);
        });
    }

    private void abrirTelaUsuario() {
        SwingUtilities.invokeLater(() -> {
            TelaUsuario telaUsuario = new TelaUsuario();
            telaUsuario.setVisible(true);
            dispose(); 
        });
    }

    private void abrirTelaAtendente() {
        SwingUtilities.invokeLater(() -> {
            TelaAtendente telaAtendente = new TelaAtendente();
            telaAtendente.setVisible(true);
            dispose(); // Fecha a tela de login
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin tela = new TelaLogin();
            tela.setVisible(true);
        });
    }
}
