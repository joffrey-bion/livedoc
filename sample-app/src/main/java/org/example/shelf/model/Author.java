package org.example.shelf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.example.shelf.documentation.DocumentationConstants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@ApiType(name = "Author",
        group = DocumentationConstants.GROUP_LIBRARY,
        description = "Represents an author. Every author has a list of <code>Book</code>s")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiTypeProperty
    private Long id;

    @Column(name = "name")
    @NotBlank
    @Length(min = 5, max = 20)
    @ApiTypeProperty(required = true, description = "Name and surname of the author")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ApiTypeProperty
    private List<Book> books = new ArrayList<Book>();

    public Author() {
    }

    public Author(Long id, String name, List<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public void addBook(Book book) {
        this.getBooks().add(book);
        book.setAuthor(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
