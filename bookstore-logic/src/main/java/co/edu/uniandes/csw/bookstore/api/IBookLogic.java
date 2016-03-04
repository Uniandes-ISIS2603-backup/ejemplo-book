package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.List;

public interface IBookLogic {

    public List<BookEntity> getBooks();

    public BookEntity getBook(Long id) throws BusinessLogicException;

    public BookEntity createBook(BookEntity entity);

    public BookEntity updateBook(BookEntity entity);

    public void deleteBook(Long id);
}
