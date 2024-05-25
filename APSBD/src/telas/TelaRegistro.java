import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TelaRegistro extends JFrame implements ActionListener {
    private JTextField campoNomeCompleto;
    private JTextField campoCPF;
    private JTextField campoEmail;
    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JButton botaoRegistrar;

    public TelaRegistro() {
        setTitle("Registro de Usuário");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(6, 2));

        painel.add(new JLabel("Nome Completo:"));
        campoNomeCompleto = new JTextField();
        painel.add(campoNomeCompleto);

        painel.add(new JLabel("CPF:"));
        campoCPF = new JTextField();
        painel.add(campoCPF);

        painel.add(new JLabel("E-mail:"));
        campoEmail = new JTextField();
        painel.add(campoEmail);

        painel.add(new JLabel("Login:"));
        campoLogin = new JTextField();
        painel.add(campoLogin);

        painel.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painel.add(campoSenha);

        botaoRegistrar = new JButton("Registrar");
        botaoRegistrar.addActionListener(this);
        painel.add(botaoRegistrar);

        add(painel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoRegistrar) {
            registrarUsuario();
        }
    }

    private void registrarUsuario() {
        String nomeCompleto = campoNomeCompleto.getText();
        String cpf = campoCPF.getText();
        String email = campoEmail.getText();
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());
        
        if (nomeCompleto.isEmpty() || cpf.isEmpty() || email.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }
        
        try {
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO Usuarios (nome_completo, cpf, email, login, senha) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, nomeCompleto);
            stmt.setString(2, cpf);
            stmt.setString(3, email);
            stmt.setString(4, login);
            stmt.setString(5, senha);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar usuário!");
            }
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaRegistro tela = new TelaRegistro();
            tela.setVisible(true);
        });
    }
}
