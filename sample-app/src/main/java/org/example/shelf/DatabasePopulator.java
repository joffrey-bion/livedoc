package org.example.shelf;

import java.util.ArrayList;

import org.example.shelf.model.Author;
import org.example.shelf.model.Book;
import org.example.shelf.model.User;
import org.example.shelf.repository.AuthorRepository;
import org.example.shelf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabasePopulator implements CommandLineRunner {

    private final AuthorRepository authorRepository;

    private final UserRepository userRepository;

    @Autowired
    public DatabasePopulator(AuthorRepository authorRepository, UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
    }

    public void run(String... arg0) {
        Author hornby = new Author(null, "Nick Horby", new ArrayList<>());
        hornby.addBook(new Book(null, "High fidelty", hornby, 5.99D));
        hornby.addBook(new Book(null, "A long way down", hornby, 0.99D));

        Author smith = new Author(null, "Wilbur Smith", new ArrayList<>());
        smith.addBook(new Book(null, "Desert god", smith, 1.99D));

        authorRepository.save(hornby);
        authorRepository.save(smith);

        userRepository.save(new User("user-one"));
        userRepository.save(new User("user-two"));
        userRepository.save(new User("user-three"));
    }
}
