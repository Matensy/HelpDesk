import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CadastroPatrimonio {
    public static boolean cadastrarPatrimonio(Connection conexao, String nome, String descricao, String localizacao, String status) {
        String sql = "INSERT INTO patrimonio (nome, descricao, localizacao, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, localizacao);
            stmt.setString(4, status);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}