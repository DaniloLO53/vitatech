package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.User;
import org.split.app.vitatech.models.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {

    Page<UserActivity> findByUserOrderByPerformedAtDesc(User user, Pageable pageable);

    List<UserActivity> findByUserAndPerformedAtBetweenOrderByPerformedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    Optional<UserActivity> findByIdAndUser(Integer id, User user);
}