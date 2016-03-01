package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IAuthorLogic;
import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.persistence.AuthorPersistence;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AuthorLogic implements IAuthorLogic{

    @Inject
    private AuthorPersistence persistence;

    @Inject
    IBookLogic bookLogic;

    @Inject
    private BookPersistence bookPersistence;

    @Override
    public List<AuthorEntity> getAuthors() {
        return persistence.findAll();
    }

    @Override
    public AuthorEntity getAuthor(Long id) {
        return persistence.find(id);
    }

    @Override
    public AuthorEntity createAuthor(AuthorEntity entity) {
        persistence.create(entity);
        return entity;
    }

    @Override
    public AuthorEntity updateAuthor(AuthorEntity entity) {
        persistence.update(entity);
        return entity;
    }

    @Override
    public void deleteAuthor(Long id) {
        persistence.delete(id);
    }

    @Override
    public BookEntity addBook(Long bookId, Long authorId) {
        bookLogic.addAuthor(authorId, bookId);
        return bookPersistence.find(bookId);
    }

    @Override
    public void removeBook(Long bookId, Long authorId) {
        bookLogic.removeAuthor(authorId, bookId);
    }

    @Override
    public List<BookEntity> replaceBooks(List<BookEntity> books, Long authorId) {
        List<BookEntity> bookList = bookPersistence.findAll();
        AuthorEntity author = persistence.find(authorId);
        for (BookEntity book : bookList) {
            if (books.contains(book)) {
                if (!book.getAuthors().contains(author)) {
                    bookLogic.addAuthor(authorId, book.getId());
                }
            } else {
                bookLogic.removeAuthor(authorId, book.getId());
            }
        }
        author.setBooks(books);
        return author.getBooks();
    }

    @Override
    public List<BookEntity> getBooks(Long authorId) {
        return persistence.find(authorId).getBooks();
    }

    @Override
    public BookEntity getBook(Long authorId, Long bookId) {
        List<BookEntity> books = persistence.find(authorId).getBooks();
        BookEntity book = new BookEntity();
        book.setId(bookId);
        int index = books.indexOf(book);
        if (index >= 0) {
            return books.get(index);
        }
        return null;
    }

    @Override
    public List<AuthorEntity> findByEditorial(Long editorialId){
        return persistence.findByEditorial(editorialId);
    }
}
