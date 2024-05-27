import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaVisualizarChamadosUsuario extends JFrame {
    private JTextArea areaChamados;

    public TelaVisualizarChamadosUsuario() {
        setTitle("Chamados");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        areaChamados = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(areaChamados);

        add(scrollPane, BorderLayout.CENTER);

        exibirChamados();
    }

    private void exibirChamados() {
        try {
            Connection conexao = ConexaoBD.conectar();
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Chamados WHERE usuario_id = 1"); // Você pode substituir pelo id do usuário logado
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaVisualizarChamadosUsuario tela = new TelaVisualizarChamadosUsuario();
            tela.setVisible(true);
        });
    }
}
