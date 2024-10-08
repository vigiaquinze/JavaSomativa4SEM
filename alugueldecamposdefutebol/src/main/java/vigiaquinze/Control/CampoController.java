package vigiaquinze.Control;

import vigiaquinze.Connection.ConnectionFactory;
import vigiaquinze.Model.Campo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CampoController {

    public void adicionarCampo(Campo campo) {
        String sql = "INSERT INTO campo (nome, local, preco) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, campo.getNome());
            stmt.setString(2, campo.getLocal());
            stmt.setDouble(3, campo.getPreco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Campo buscarCampo(int id) {
        String sql = "SELECT * FROM campo WHERE id = ?";
        Campo campo = null;
    
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                campo = new Campo(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("local"),
                    rs.getDouble("preco")
                );
    
                // Se necessário, você pode buscar e adicionar as reservas ao campo aqui
                // Exemplo: carregarReservas(campo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return campo; // Pode retornar null se não encontrar o campo
    }
    
    

    public void atualizarCampo(Campo campo) {
        String sql = "UPDATE campo SET nome = ?, local = ?, preco = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, campo.getNome());
            stmt.setString(2, campo.getLocal());
            stmt.setDouble(3, campo.getPreco());
            stmt.setInt(4, campo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarCampo(int id) {
        String sql = "DELETE FROM campo WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verificarDisponibilidade(int campoId, Date data, Time horaInicio, Time horaFim) {
        String sql = "SELECT * FROM reserva WHERE campo_id = ? AND data = ? AND " +
                     "(hora_inicio < ? AND hora_fim > ?)";
        boolean disponivel = true;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, campoId);
            stmt.setDate(2, data);
            stmt.setTime(3, horaFim);
            stmt.setTime(4, horaInicio);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                disponivel = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disponivel;
    }

    // Método para listar todos os campos
    public List<Campo> listarCampos() {
        String sql = "SELECT * FROM campo";
        List<Campo> campos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Campo campo = new Campo(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getDouble("preco")
                );
                campos.add(campo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campos;
    }

    // Método para buscar campos pelo nome
    public List<Campo> buscarCamposPorNome(String nome) {
        String sql = "SELECT * FROM campo WHERE nome ILIKE ?";
        List<Campo> campos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Campo campo = new Campo(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getDouble("preco")
                );
                campos.add(campo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campos;
    }
}
