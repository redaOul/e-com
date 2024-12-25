package com.redaeilco.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redaeilco.ecommerce.model.User;

/*
 * Interface for CRUD operations on User entities,
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /*
     * Default Methods
     * Save Operations: save
     * Delete Operations: delete, deleteById, deleteAll
     * Find Operations: findById, findAll, findAllById, count
     * Existence Check: existsById
     */

    /*
     * Spring Data JPA interprets method names to generate queries.
     * findBy: This keyword tells Spring Data JPA to generate a query method.
     * Username: This is the property of the User entity that you want to search by.
     * Generated Query: SELECT * FROM users WHERE username = ?;
     * findByEmailEndingWith, findByRoleAndStatus, findByCreatedDateAfter
     */
    Optional<User> findByUsername(String username);
}
