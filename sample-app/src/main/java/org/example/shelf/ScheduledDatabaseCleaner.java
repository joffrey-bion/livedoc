package org.example.shelf;

import org.example.shelf.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledDatabaseCleaner {

    private final AuthorRepository authorRepository;

    @Autowired
    public ScheduledDatabaseCleaner(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void cleanAndRestore() {
        authorRepository.deleteByIdGreaterThan(2L);
    }

}
