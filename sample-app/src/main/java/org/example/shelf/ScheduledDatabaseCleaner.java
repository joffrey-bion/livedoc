package org.example.shelf;

import javax.transaction.Transactional;

import org.example.shelf.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledDatabaseCleaner {

    private final AuthorRepository authorRepository;

    @Autowired
    public ScheduledDatabaseCleaner(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void cleanAndRestore() {
        authorRepository.deleteByIdGreaterThan(2L);
    }

}
