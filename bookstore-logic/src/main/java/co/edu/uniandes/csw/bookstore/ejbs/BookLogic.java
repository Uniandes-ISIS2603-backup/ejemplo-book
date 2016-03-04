package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BookLogic implements IBookLogic {

    @Inject
    private BookPersistence persistence;

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
}
