import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TelaAtendente extends JFrame implements ActionListener {
    private JTextArea areaChamados;
    private JButton botaoListar;
    private JButton botaoBuscar;
    private JTextField campoBuscar;
    private JButton botaoVisualizar;
    private JButton botaoResponder;
    private JLabel labelQuantidade;

    public TelaAtendente() {
        setTitle("Atendente");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());

        areaChamados = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(areaChamados);
        painel.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 5));
        botaoListar = new JButton("Listar");
        botaoListar.addActionListener(this);
        painelBotoes.add(botaoListar);

        botaoBuscar = new JButton("Buscar");
        botaoBuscar.addActionListener(this);
        painelBotoes.add(botaoBuscar);

        campoBuscar = new JTextField();
        painelBotoes.add(campoBuscar);

        botaoVisualizar = new JButton("Visualizar");
        botaoVisualizar.addActionListener(this);
        painelBotoes.add(botaoVisualizar);

        botaoResponder = new JButton("Responder");
        botaoResponder.addActionListener(this);
        painelBotoes.add(botaoResponder);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        labelQuantidade = new JLabel();
        painel.add(labelQuantidade, BorderLayout.NORTH);

        add(painel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoListar) {
            listarChamados();
        } else if (e.getSource() == botaoBuscar) {
            buscarChamados(campoBuscar.getText());
        } else if (e.getSource() == botaoVisualizar) {
            visualizarChamado();
        } else if (e.getSource() == botaoResponder) {
            responderChamado();
        }
    }

    private void listarChamados() {
        try {
            Connection conexao = ConexaoBD.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Chamados.id, Chamados.descricao, Usuarios.nome " +
                                             "FROM Chamados " +
                                             "JOIN Usuarios ON Chamados.usuario_id = Usuarios.id " +
                                             "WHERE Chamados.status = 'aberto'");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n");
                sb.append("Usuário: ").append(rs.getString("nome")).append("\n");
                sb.append("Descrição: ").append(rs.getString("descricao")).append("\n\n");
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
    

    private void buscarChamados(String termoBusca) {
        try {
            Connection conexao = ConexaoBD.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Chamados WHERE descricao LIKE '%" + termoBusca + "%'");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n");
                sb.append("Descrição: ").append(rs.getString("descricao")).append("\n\n");
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
                JOptionPane.showMessageDialog(this, "Erro ao conectarao banco de dados!");
            }
        }
    }

    private void responderChamado() {
        String idChamadoStr = JOptionPane.showInputDialog(this, "Digite o ID do chamado que deseja responder:");
        if (idChamadoStr != null && !idChamadoStr.isEmpty()) {
            int idChamado = Integer.parseInt(idChamadoStr);
            String solucao = JOptionPane.showInputDialog(this, "Digite a solução para o chamado:");
            if (solucao != null && !solucao.isEmpty()) {
                try {
                    Connection conexao = ConexaoBD.conectar();
                    PreparedStatement stmt = conexao.prepareStatement("UPDATE Chamados SET status = 'resolvido', resposta = ? WHERE id = ? AND status = 'aberto'");
                    stmt.setString(1, solucao);
                    stmt.setInt(2, idChamado);
                    int linhasAfetadas = stmt.executeUpdate();
                    if (linhasAfetadas > 0) {
                        JOptionPane.showMessageDialog(this, "Chamado respondido com sucesso!");
                        // Agora, vamos exibir a lista atualizada de chamados após a resposta ser registrada
                        listarChamados();
                    } else {
                        JOptionPane.showMessageDialog(this, "Chamado não encontrado ou já foi respondido!");
                    }
                    stmt.close();
                    conexao.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Solução não pode estar vazia!");
            }
        }
    }
    
    
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaAtendente tela = new TelaAtendente();
            tela.setVisible(true);
        });
    }
}

