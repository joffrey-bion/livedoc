package org.example.shelf.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.example.shelf.documentation.DocumentationConstants;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@Entity
@ApiType(name = "Book",
        group = DocumentationConstants.GROUP_LIBRARY,
        description = "Represents a book. Every book has an <code>Author</code> and a price.")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiTypeProperty(description = "The book's ID", order = 4)
    private Long id;

    @Column(name = "title")
    @ApiTypeProperty(description = "The book's title", order = 2)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @ApiTypeProperty(description = "The book's author", order = 1)
    private Author author;

    @Column(name = "price")
    @ApiTypeProperty(required = true, format = "Must be a double", description = "The price of the book")
    private Double price;

    public Book() {
    }

    public Book(Long id, String title, Author author, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(price,
                book.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, price);
    }
}
