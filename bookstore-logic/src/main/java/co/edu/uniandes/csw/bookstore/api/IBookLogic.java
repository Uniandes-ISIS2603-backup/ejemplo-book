package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.List;

public interface IBookLogic {

    public List<BookEntity> getBooks();

    public BookEntity getBook(Long id);

    public BookEntity createBook(BookEntity entity) throws BusinessLogicException;

    public BookEntity updateBook(BookEntity entity) throws BusinessLogicException;

    public void deleteBook(Long id);

    public List<AuthorEntity> getAuthors(Long bookId);

    public AuthorEntity getAuthor(Long bookId, Long authorId);

    public AuthorEntity addAuthor(Long authorId, Long bookId) throws BusinessLogicException;

    public void removeAuthor(Long authorId, Long bookId);

    public List<AuthorEntity> replaceAuthors(List<AuthorEntity> authors, Long bookId) throws BusinessLogicException;

    

}
