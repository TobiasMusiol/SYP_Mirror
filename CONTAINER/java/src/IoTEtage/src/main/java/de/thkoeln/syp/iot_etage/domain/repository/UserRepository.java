package de.thkoeln.syp.iot_etage.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thkoeln.syp.iot_etage.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByName(String name);
  boolean existsByName(String name);
}
