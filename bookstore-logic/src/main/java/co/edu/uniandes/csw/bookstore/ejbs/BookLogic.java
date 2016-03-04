package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.AuthorPersistence;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BookLogic implements IBookLogic {

    @Inject
    private BookPersistence persistence;

    @Inject
    private AuthorPersistence authorPersistence;

    @Override
    public List<BookEntity> getBooks() {
        return persistence.findAll();
    }

    @Override
    public BookEntity getBook(Long id) throws BusinessLogicException {
        BookEntity book = persistence.find(id);
        if (book == null) {
            throw new BusinessLogicException("There's no book with requested id");
        }
        return book;
    }

    @Override
    public BookEntity createBook(BookEntity entity) {
        persistence.create(entity);
        return entity;
    }

    @Override
    public BookEntity updateBook(BookEntity entity) {
        BookEntity newEntity = entity;
        return persistence.update(newEntity);
    }

    @Override
    public void deleteBook(Long id) {
        persistence.delete(id);
    }

    @Override
    public List<AuthorEntity> getAuthors(Long bookId) {
        return persistence.find(bookId).getAuthors();
    }

    @Override
    public AuthorEntity getAuthor(Long bookId, Long authorId) {
        List<AuthorEntity> authors = persistence.find(bookId).getAuthors();
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(authorId);
        int index = authors.indexOf(authorEntity);
        if (index >= 0) {
            return authors.get(index);
        }
        return null;
    }

    @Override
    public AuthorEntity addAuthor(Long authorId, Long bookId) {
        BookEntity bookEntity = persistence.find(bookId);
        AuthorEntity authorEntity = authorPersistence.find(authorId);
        bookEntity.getAuthors().add(authorEntity);
        return authorEntity;
    }

    @Override
    public void removeAuthor(Long authorId, Long bookId) {
        BookEntity bookEntity = persistence.find(bookId);
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(authorId);
        bookEntity.getAuthors().remove(authorEntity);
    }

    @Override
    public List<AuthorEntity> replaceAuthors(List<AuthorEntity> authors, Long bookId) {
        BookEntity bookEntity = persistence.find(bookId);
        List<AuthorEntity> authorList = authorPersistence.findAll();
        bookEntity.setAuthors(authors);
        return bookEntity.getAuthors();
    }
}
