import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    // URL do banco de dados SQLite
    private static final String URL = "jdbc:sqlite:/home/italo/Documentos/DB_TP_4";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso.");

                // Adicionar dados na tabela PESSOA
                insertPerson(conn, "Italo Hissa", "italo@example.com");
                insertPerson(conn, "Olati Hissa", "olati@example.com");
                insertPerson(conn, "Michu K", "michu@example.com");

                // Atualizar dados na tabela PESSOA
                updatePerson(conn, "italo@example.com", "Italo Gothardo Tavares Hissa");

                // Ler e exibir dados da tabela PESSOA
                readPeople(conn);

                // Deletar dados na tabela PESSOA
                deletePerson(conn, "olati@example.com");

                // Ler e exibir dados após exclusão
                readPeople(conn);

                // Adicionar dados na tabela CONQUISTAS
                insertConquista(conn, 1, "Certificado", "Ouro");
                insertConquista(conn, 2, "Medalha", "Prata");

                // Atualizar dados na tabela CONQUISTAS
                updateConquista(conn, 1, "Certificado", "Platina");

                // Ler e exibir dados da tabela CONQUISTAS
                readConquistas(conn);

                // Deletar dados na tabela CONQUISTAS
                deleteConquista(conn, 2);

                // Ler e exibir dados após exclusão
                readConquistas(conn);

                // Buscar registros na tabela PESSOA contendo uma substring no nome
                searchPeopleBySubstring(conn, "Hissa");

                // Inserir múltiplos registros na tabela PESSOA
                List<String[]> people = List.of(
                    new String[] {"Lucas Pereira", "lucas@example.com"},
                    new String[] {"Maria Oliveira", "maria@example.com"}
                );
                insertMultiplePeople(conn, people);

            } else {
                System.out.println("Não foi possível estabelecer a conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    // Insere uma pessoa na tabela PESSOA
    private static void insertPerson(Connection conn, String nome, String email) {
        String sql = "INSERT INTO PESSOA(nome, email) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Pessoa inserida com sucesso: " + nome + " (" + email + ")");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir a pessoa.");
            e.printStackTrace();
        }
    }

    // Insere múltiplas pessoas na tabela PESSOA
    private static void insertMultiplePeople(Connection conn, List<String[]> people) {
        String sql = "INSERT INTO PESSOA(nome, email) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] person : people) {
                pstmt.setString(1, person[0]);
                pstmt.setString(2, person[1]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Múltiplas pessoas inseridas com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir múltiplas pessoas.");
            e.printStackTrace();
        }
    }

    // Atualiza o nome de uma pessoa na tabela PESSOA
    private static void updatePerson(Connection conn, String email, String newName) {
        String sql = "UPDATE PESSOA SET nome = ? WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pessoa atualizada com sucesso: " + newName + " (" + email + ")");
            } else {
                System.out.println("Nenhuma pessoa encontrada com o email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar a pessoa.");
            e.printStackTrace();
        }
    }

    // Lê e exibe todas as pessoas da tabela PESSOA
    private static void readPeople(Connection conn) {
        String sql = "SELECT nome, email FROM PESSOA";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Lista de pessoas:");
            while (rs.next()) {
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                System.out.println("Nome: " + nome + ", Email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao ler os dados.");
            e.printStackTrace();
        }
    }

    // Deleta uma pessoa da tabela PESSOA
    private static void deletePerson(Connection conn, String email) {
        String sql = "DELETE FROM PESSOA WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pessoa deletada com sucesso: " + email);
            } else {
                System.out.println("Nenhuma pessoa encontrada com o email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar a pessoa.");
            e.printStackTrace();
        }
    }

    // Insere uma conquista na tabela CONQUISTAS
    private static void insertConquista(Connection conn, int idPessoa, String tipo, String raridade) {
        String sql = "INSERT INTO CONQUISTAS(id_pessoa, tipo, raridade) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPessoa);
            pstmt.setString(2, tipo);
            pstmt.setString(3, raridade);
            pstmt.executeUpdate();
            System.out.println("Conquista inserida com sucesso: " + tipo + " (" + raridade + ")");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir a conquista.");
            e.printStackTrace();
        }
    }

    // Atualiza uma conquista na tabela CONQUISTAS
    private static void updateConquista(Connection conn, int idPessoa, String tipo, String newRaridade) {
        String sql = "UPDATE CONQUISTAS SET raridade = ? WHERE id_pessoa = ? AND tipo = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRaridade);
            pstmt.setInt(2, idPessoa);
            pstmt.setString(3, tipo);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Conquista atualizada com sucesso: " + tipo + " (" + newRaridade + ")");
            } else {
                System.out.println("Nenhuma conquista encontrada com id_pessoa: " + idPessoa + " e tipo: " + tipo);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar a conquista.");
            e.printStackTrace();
        }
    }

    // Lê e exibe todas as conquistas da tabela CONQUISTAS
    private static void readConquistas(Connection conn) {
        String sql = "SELECT id_pessoa, tipo, raridade FROM CONQUISTAS";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Lista de conquistas:");
            while (rs.next()) {
                int idPessoa = rs.getInt("id_pessoa");
                String tipo = rs.getString("tipo");
                String raridade = rs.getString("raridade");
                System.out.println("ID Pessoa: " + idPessoa + ", Tipo: " + tipo + ", Raridade: " + raridade);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao ler os dados.");
            e.printStackTrace();
        }
    }

    // Deleta uma conquista da tabela CONQUISTAS
    private static void deleteConquista(Connection conn, int idPessoa) {
        String sql = "DELETE FROM CONQUISTAS WHERE id_pessoa = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPessoa);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Conquista deletada com sucesso para id_pessoa: " + idPessoa);
            } else {
                System.out.println("Nenhuma conquista encontrada com id_pessoa: " + idPessoa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar a conquista.");
            e.printStackTrace();
        }
    }

    // Busca registros na tabela PESSOA que contém uma substring no nome
    private static void searchPeopleBySubstring(Connection conn, String substring) {
        String sql = "SELECT nome, email FROM PESSOA WHERE nome LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + substring + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Lista de pessoas com substring '" + substring + "':");
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    System.out.println("Nome: " + nome + ", Email: " + email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar os dados.");
            e.printStackTrace();
        }
    }
}