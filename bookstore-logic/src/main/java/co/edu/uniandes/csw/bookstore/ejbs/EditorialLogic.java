package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IEditorialLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import co.edu.uniandes.csw.bookstore.persistence.EditorialPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class EditorialLogic implements IEditorialLogic {

    @Inject
    private EditorialPersistence persistence;

    @Inject
    private BookPersistence bookPersistence;

    @Override
    public List<EditorialEntity> getEditorials() {
        return persistence.findAll();
    }

    @Override
    public EditorialEntity getEditorial(Long id) {
        return persistence.find(id);
    }

    @Override
    public EditorialEntity createEditorial(EditorialEntity entity) {
        persistence.create(entity);
        return entity;
    }

    @Override
    public EditorialEntity updateEditorial(EditorialEntity entity) {
        entity = persistence.update(entity);
        return entity;
    }

    @Override
    public void deleteEditorial(Long id) {
        persistence.delete(id);
    }

    @Override
    public BookEntity addBook(Long bookId, Long editorialId) {
        EditorialEntity editorialEntity = persistence.find(editorialId);
        BookEntity bookEntity = bookPersistence.find(bookId);
        bookEntity.setEditorial(editorialEntity);
        return bookEntity;
    }

    @Override
    public void removeBook(Long bookId, Long editorialId) {
        EditorialEntity editorialEntity = persistence.find(editorialId);
        BookEntity book = bookPersistence.find(bookId);
        book.setEditorial(null);
        editorialEntity.getBooks().remove(book);
    }

    @Override
    public List<BookEntity> replaceBooks(List<BookEntity> books, Long editorialId) {
        EditorialEntity editorial = persistence.find(editorialId);
        List<BookEntity> bookList = bookPersistence.findAll();
        for (BookEntity book : bookList) {
            if (books.contains(book)) {
                book.setEditorial(editorial);
            } else if (book.getEditorial() != null && book.getEditorial().equals(editorial)) {
                book.setEditorial(null);
            }
        }
        return books;
    }

    @Override
    public List<BookEntity> getBooks(Long editorialId) {
        return persistence.find(editorialId).getBooks();
    }

    @Override
    public BookEntity getBook(Long editorialId, Long bookId) {
        List<BookEntity> books = persistence.find(editorialId).getBooks();
        BookEntity book = new BookEntity();
        book.setId(bookId);
        int index = books.indexOf(book);
        if (index >= 0) {
            return books.get(index);
        }
        return null;
    }
}
