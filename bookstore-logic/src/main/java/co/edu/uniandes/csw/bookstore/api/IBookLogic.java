package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import java.util.List;

public interface IBookLogic {

    public List<BookEntity> getBooks();

    public BookEntity getBook(Long id);

    public BookEntity createBook(BookEntity entity);

    public BookEntity updateBook(BookEntity entity);

    public void deleteBook(Long id);

    public List<AuthorEntity> getAuthors(Long bookId);

    public AuthorEntity getAuthor(Long bookId, Long authorId);

    public AuthorEntity addAuthor(Long authorId, Long bookId);

    public void removeAuthor(Long authorId, Long bookId);

    public List<AuthorEntity> replaceAuthors(List<AuthorEntity> authors, Long bookId);

}
