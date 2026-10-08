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

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByRole(UserRole role);

    Page<User> findAllByRole(UserRole role, Pageable pageable);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE u.role = :role AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchUsersByRoleAndName(@Param("role") UserRole role, @Param("name") String name);

    Page<User> findByRoleAndNameContainingIgnoreCase(UserRole role, String name, Pageable pageable);
}