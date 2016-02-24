package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import java.util.List;

public interface IBookLogic {

    public List<BookEntity> getBooks();

    public BookEntity getBook(Long id);

    public BookEntity createBook(BookEntity entity);

    public BookEntity updateBook(BookEntity entity);

    public void deleteBook(Long id);
}
