import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TelaUsuario extends JFrame implements ActionListener {
    private JTextArea areaChamados;
    private JButton botaoListar;
    private JButton botaoVisualizar;
    private JButton botaoAbrirChamado;
    private JLabel labelMeusChamados;

    public TelaUsuario() {
        setTitle("Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());

        areaChamados = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(areaChamados);
        painel.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3));
        botaoListar = new JButton("Listar Meus Chamados");
        botaoListar.addActionListener(this);
        painelBotoes.add(botaoListar);

        botaoVisualizar = new JButton("Visualizar");
        botaoVisualizar.addActionListener(this);
        painelBotoes.add(botaoVisualizar);

        botaoAbrirChamado = new JButton("Abrir Chamado");
        botaoAbrirChamado.addActionListener(this);
        painelBotoes.add(botaoAbrirChamado);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        labelMeusChamados = new JLabel("Meus Chamados:");
        painel.add(labelMeusChamados, BorderLayout.NORTH);

        add(painel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoListar) {
            listarMeusChamados();
        } else if (e.getSource() == botaoVisualizar) {
            visualizarChamado();
        } else if (e.getSource() == botaoAbrirChamado) {
            abrirChamado();
        }
    }

    private void listarMeusChamados() {
        try {
            Connection conexao = ConexaoBD.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Chamados.id, Chamados.descricao, Chamados.resposta " +
                                             "FROM Chamados " +
                                             "WHERE Chamados.usuario_id = 1"); // Substitua 1 pelo ID do usuário logado
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n");
                sb.append("Descrição: ").append(rs.getString("descricao")).append("\n");
                sb.append("Resposta: ").append(rs.getString("resposta")).append("\n\n");
            }
            areaChamados.setText(sb.toString());
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
        }
    }
    
    

    private void visualizarChamado() {
        String idChamadoStr = JOptionPane.showInputDialog(this, "Digite o ID do chamado que deseja visualizar:");
        if (idChamadoStr != null && !idChamadoStr.isEmpty()) {
            int idChamado = Integer.parseInt(idChamadoStr);
            try {
                Connection conexao = ConexaoBD.conectar();
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Chamados WHERE id = " + idChamado);
                StringBuilder sb = new StringBuilder();
                if (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id")).append("\n");
                    sb.append("Descrição: ").append(rs.getString("descricao")).append("\n\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Chamado não encontrado!");
                }
                areaChamados.setText(sb.toString());
                rs.close();
                stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
            }
        }
    }

    private void abrirChamado() {
        String descricao = JOptionPane.showInputDialog(this, "Digite a descrição do novo chamado:");
        if (descricao != null && !descricao.isEmpty()) {
            try {
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement stmt = conexao.prepareStatement("INSERT INTO Chamados (descricao, usuario_id, status) VALUES (?, ?, ?)");
                stmt.setString(1, descricao);
                stmt.setInt(2, 1); // Substitua 1 pelo ID do usuário logado
                stmt.setString(3, "aberto");
                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Chamado aberto com sucesso!");
                    listarMeusChamados();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao abrir chamado!");
                }
                stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaUsuario tela = new TelaUsuario();
            tela.setVisible(true);
        });
    }
}
