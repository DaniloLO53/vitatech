package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Food;
import org.split.app.vitatech.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    // 1. Busca Textual Paginada (Requisito de Desempenho)
    // Busca alimentos pelo nome ignorando maiúsculas/minúsculas.
    // Retorna uma "Página" (Page) para otimizar o front-end e o banco de dados.
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 2. Listar Apenas Alimentos do Sistema
    // Traz a base de dados padrão da aplicação (onde createdBy é null).
    Page<Food> findByCreatedByIsNull(Pageable pageable);

    // 3. Listar Receitas Personalizadas do Usuário
    // Retorna os alimentos que foram criados especificamente por um usuário.
    Page<Food> findByCreatedBy(User user, Pageable pageable);

    // 4. Busca Textual Inteligente (Sistema + Usuário Atual)
    // A query mais importante para a barra de pesquisa do front-end:
    // Busca alimentos que contenham o texto digitado E que sejam do sistema (null)
    // OU que tenham sido criados pelo próprio usuário que está pesquisando.
    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND (f.createdBy IS NULL OR f.createdBy = :user)")
    Page<Food> searchAvailableFoodsForUser(@Param("name") String name, @Param("user") User user, Pageable pageable);

    // 5. Verificar duplicidade de receita do usuário
    // Útil no momento do cadastro para evitar que o usuário crie dois alimentos com o mesmo nome.
    boolean existsByNameIgnoreCaseAndCreatedBy(String name, User user);
}