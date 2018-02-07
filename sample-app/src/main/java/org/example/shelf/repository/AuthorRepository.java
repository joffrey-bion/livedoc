package org.example.shelf.repository;

import org.example.shelf.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    void deleteByIdGreaterThan(Long id);

}
