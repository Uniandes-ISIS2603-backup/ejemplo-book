package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.converters.AuthorConverter;
import co.edu.uniandes.csw.bookstore.converters.BookConverter;
import co.edu.uniandes.csw.bookstore.dtos.AuthorDTO;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.providers.StatusCreated;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger logger = Logger.getLogger(BookResource.class.getName());

    @Inject
    private IBookLogic bookLogic;

    /**
     * Obtiene la lista de los registros de Book.
     *
     * @return Colección de objetos de BookDTO cada uno con sus respectivos Review
     * @generated
     */
    @GET
    public List<BookDTO> getBooks() {
        logger.info("Se ejecuta método getBooks");
        List<BookEntity> books = bookLogic.getBooks();
        return BookConverter.listEntity2DTO(books);
    }

    /**
     * Obtiene los datos de un objeto de Book a partir de su ID.
     *
     * @param id Identificador del objeto a consultar
     * @return Instancia de BookDTO con los datos del Book consultado y sus Review
     * @generated
     */
    @GET
    @Path("{id: \\d+}")
    public BookDTO getBook(@PathParam("id") Long id) {
        logger.log(Level.INFO, "Se ejecuta método getBook con id={0}", id);
        BookEntity book = bookLogic.getBook(id);
        return BookConverter.fullEntity2DTO(book);
    }

    /**
     * Se encarga de crear un book en la base de datos.
     *
     * @param dto Objeto de BookDTO con los datos nuevos
     * @return Objeto de BookDTO con los datos nuevos y su ID.
     * @generated
     */
    @POST
    @StatusCreated
    public BookDTO createBook(BookDTO dto) {
        logger.info("Se ejecuta método createBook");
        BookEntity entity = BookConverter.fullDTO2Entity(dto);
        BookEntity newEntity;
        try {
            newEntity = bookLogic.createBook(entity);
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
        return BookConverter.fullEntity2DTO(newEntity);
    }

    /**
     * Actualiza la información de un objeto de Book.
     *
     * @param id Identificador del objeto de Book a modificar
     * @param dto Representación Basic de book con los nuevos datos
     * @return Instancia de BookDTO con los datos actualizados.
     * @generated
     */
    @PUT
    @Path("{id: \\d+}")
    public BookDTO updateBook(@PathParam("id") Long id, BookDTO dto) {
        logger.log(Level.INFO, "Se ejecuta método updateBook con id={0}", id);
        BookEntity entity = BookConverter.fullDTO2Entity(dto);
        entity.setId(id);
        BookEntity oldEntity = bookLogic.getBook(id);
        entity.setAuthors(oldEntity.getAuthors());
        entity.setPrizes(oldEntity.getPrizes());
        try {
            BookEntity savedBook = bookLogic.updateBook(entity);
            return BookConverter.fullEntity2DTO(savedBook);
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Elimina un objeto de Book de la base de datos.
     *
     * @param id Identificador del objeto a eliminar.
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteBook(@PathParam("id") Long id) {
        logger.log(Level.INFO, "Se ejecuta método deleteBook con id={0}", id);
        bookLogic.deleteBook(id);
    }

    /**
     * Obtiene una colección de objetos de AuthorDTO asociados a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @return Colección de objetos de AuthorDTO en representación basic
     * @generated
     */
    @GET
    @Path("{bookId: \\d+}/authors")
    public List<AuthorDTO> listAuthors(@PathParam("bookId") Long bookId) {
        List<AuthorEntity> authors = bookLogic.getAuthors(bookId);
        return AuthorConverter.listEntity2DTO(authors);
    }

    /**
     * Obtiene un objeto de Author asociada a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @param authorId Identificador del objeto de Author
     * @generated
     */
    @GET
    @Path("{bookId: \\d+}/authors/{authorId: \\d+}")
    public AuthorDTO getAuthors(@PathParam("bookId") Long bookId, @PathParam("authorId") Long authorId) {
        AuthorEntity author = bookLogic.getAuthor(bookId, authorId);
        return AuthorConverter.fullEntity2DTO(author);
    }

    /**
     * Asocia un Author existente a un Book
     *
     * @param bookId Identificador del objeto de Book
     * @param authorId Identificador del objeto de Author
     * @return Objeto de AuthorDTO en representación full que fue asociado a Book
     * @generated
     */
    @POST
    @Path("{bookId: \\d+}/authors/{authorId: \\d+}")
    public AuthorDTO addAuthors(@PathParam("bookId") Long bookId, @PathParam("authorId") Long authorId) {
        try {
            AuthorEntity author = bookLogic.addAuthor(bookId, authorId);
            return AuthorConverter.fullEntity2DTO(author);
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Remplaza los objetos de Author asociados a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @param authors Colección de objetos de AuthorDTO en representación minimum a asociar a objeto
     * de Book
     * @return Nueva colección de AuthorDTO en representación Basic
     * @generated
     */
    @PUT
    @Path("{bookId: \\d+}/authors")
    public List<AuthorDTO> replaceAuthors(@PathParam("bookId") Long bookId, List<AuthorDTO> authors) {
        try {
            List<AuthorEntity> authorList = AuthorConverter.listDTO2Entity(authors);
            List<AuthorEntity> newAuthors = bookLogic.replaceAuthors(authorList, bookId);
            return AuthorConverter.listEntity2DTO(newAuthors);
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Desasocia un Author existente de un Book existente
     *
     * @param bookId Identificador del objeto de Book
     * @param authorId Identificador del objeto de Author
     * @generated
     */
    @DELETE
    @Path("{bookId: \\d+}/authors/{authorId: \\d+}")
    public void removeAuthors(@PathParam("bookId") Long bookId, @PathParam("authorId") Long authorId) {
        bookLogic.removeAuthor(bookId, authorId);
    }

    
}
