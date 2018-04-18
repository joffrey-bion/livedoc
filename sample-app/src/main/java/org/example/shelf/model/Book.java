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

/**
 * Represents a book. Every book has an <code>Author</code> and a price.
 */
@Entity
@ApiType(group = DocumentationConstants.GROUP_LIBRARY)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "price")
    private Double price;

    public Book() {
    }

    public Book(Long id, String title, Author author, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    /**
     * @return The book's ID
     */
    @ApiTypeProperty(order = 4)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the book's title.
     *
     * @return the book's title
     */
    @ApiTypeProperty(order = 2)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the book's author.
     *
     * @return the book's author
     */
    @ApiTypeProperty(order = 1)
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Gets the price of the book.
     *
     * @return the price of the book
     */
    @ApiTypeProperty(required = true, format = "Must be a double")
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
