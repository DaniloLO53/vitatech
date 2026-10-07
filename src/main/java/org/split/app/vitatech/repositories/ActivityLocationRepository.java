package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.ActivityLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLocationRepository extends JpaRepository<ActivityLocation, Integer> {

    // 1. Listagem Ordenada
    // Carrega a lista de locais (ex: Areia, Asfalto, Calçadão) em ordem alfabética
    // para preencher os menus "dropdown" ou seleções no front-end.
    List<ActivityLocation> findAllByOrderByNameAsc();

    // 2. Busca Textual
    // Caso a lista de locais cresça no futuro, permite pesquisar rapidamente.
    List<ActivityLocation> findByNameContainingIgnoreCase(String name);
}