package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 1. Autenticação e Segurança (Spring Security / JWT)
    // Retorna um Optional para evitar NullPointerException caso o email não exista.
    Optional<User> findByEmail(String email);

    // 2. Validação de Cadastro
    // Extremamente rápido para verificar se um email já está em uso antes de tentar salvar um novo usuário.
    boolean existsByEmail(String email);

    // 3. Listagem Simples por Perfil
    // Útil para listar todos os nutricionistas disponíveis ou todos os pacientes do sistema.
    List<User> findAllByRole(UserRole role);

    // 4. Listagem Paginada por Perfil
    // Evita sobrecarga de memória e lentidão se houver milhares de pacientes.
    // O Pageable permite buscar "Página 1, trazendo 10 pacientes por vez".
    Page<User> findAllByRole(UserRole role, Pageable pageable);

    // 5. Busca Textual Flexível
    // Permite que um paciente busque um nutricionista pelo nome. O "ContainingIgnoreCase"
    // faz um "LIKE %nome%" no banco, ignorando letras maiúsculas e minúsculas.
    List<User> findByNameContainingIgnoreCase(String name);

    // 6. Consulta Customizada com JPQL (Java Persistence Query Language)
    // Exemplo de como cruzar filtros: Buscar apenas Nutricionistas que tenham um nome específico.
    @Query("SELECT u FROM User u WHERE u.role = :role AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchUsersByRoleAndName(@Param("role") UserRole role, @Param("name") String name);
}