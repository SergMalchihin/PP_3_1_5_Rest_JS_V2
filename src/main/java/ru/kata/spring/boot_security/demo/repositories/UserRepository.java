package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.Models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    Optional<User> findById(Long id);

    @Query("Select u from User u left join fetch u.roles where u.username=:username")
    User findByUsername(String username);
}
