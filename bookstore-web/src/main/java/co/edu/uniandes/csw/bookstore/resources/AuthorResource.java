package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.api.IAuthorLogic;
import co.edu.uniandes.csw.bookstore.converters.AuthorConverter;
import co.edu.uniandes.csw.bookstore.converters.BookConverter;
import co.edu.uniandes.csw.bookstore.dtos.AuthorDTO;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.providers.StatusCreated;
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

@Path("authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {
    
    private static final Logger logger = Logger.getLogger(AuthorResource.class.getName());

    @Inject
    private IAuthorLogic authorLogic;

    /**
     * Obtiene la lista de los registros de Book.
     *
     * @return Colección de objetos de AuthorDTO cada uno con sus respectivos
     * Review
     * @generated
     */
    @GET
    public List<AuthorDTO> getAuthors() {
        return AuthorConverter.listEntity2DTO(authorLogic.getAuthors());
    }

    /**
     * Obtiene los datos de una instancia de Book a partir de su ID.
     *
     * @param id Identificador de la instancia a consultar
     * @return Instancia de AuthorDTO con los datos del Book consultado y sus
     * Review
     * @generated
     */
    @GET
    @Path("{id: \\d+}")
    public AuthorDTO getAuthor(@PathParam("id") Long id) {
        try {
            return AuthorConverter.fullEntity2DTO(authorLogic.getAuthor(id));
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, "El autor no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
    }

    /**
     * Se encarga de crear un book en la base de datos.
     *
     * @param dto Objeto de AuthorDTO con los datos nuevos
     * @return Objeto de AuthorDTO con los datos nuevos y su ID.
     * @generated
     */
    @POST
    @StatusCreated
    public AuthorDTO createAuthor(AuthorDTO dto) {
        AuthorEntity entity = AuthorConverter.fullDTO2Entity(dto);
        return AuthorConverter.fullEntity2DTO(authorLogic.createAuthor(entity));
    }

    /**
     * Actualiza la información de una instancia de Book.
     *
     * @param id Identificador de la instancia de Book a modificar
     * @param dto Instancia de AuthorDTO con los nuevos datos.
     * @return Instancia de AuthorDTO con los datos actualizados.
     * @generated
     */
    @PUT
    @Path("{id: \\d+}")
    public AuthorDTO updateAuthor(@PathParam("id") Long id, AuthorDTO dto) {
        AuthorEntity entity = AuthorConverter.fullDTO2Entity(dto);
        entity.setId(id);
        try {
            AuthorEntity oldEntity = authorLogic.getAuthor(id);
            entity.setBooks(oldEntity.getBooks());
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, "El autor no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
        return AuthorConverter.fullEntity2DTO(authorLogic.updateAuthor(entity));
    }

    /**
     * Elimina una instancia de Book de la base de datos.
     *
     * @param id Identificador de la instancia a eliminar.
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteAuthor(@PathParam("id") Long id) {
        authorLogic.deleteAuthor(id);
    }

    /**
     * Obtiene una colección de instancias de BookDTO asociadas a una instancia
     * de Author
     *
     * @param authorId Identificador de la instancia de Author
     * @return Colección de instancias de BookDTO asociadas a la instancia de
     * Author
     * @generated
     */
    @GET
    @Path("{authorId: \\d+}/books")
    public List<BookDTO> listBooks(@PathParam("authorId") Long authorId) {
        return BookConverter.listEntity2DTO(authorLogic.getBooks(authorId));
    }

    /**
     * Obtiene una instancia de Book asociada a una instancia de Author
     *
     * @param authorId Identificador de la instancia de Author
     * @param bookId Identificador de la instancia de Book
     * @generated
     */
    @GET
    @Path("{authorId: \\d+}/books/{bookId: \\d+}")
    public BookDTO getBooks(@PathParam("authorId") Long authorId, @PathParam("bookId") Long bookId) {
        return BookConverter.fullEntity2DTO(authorLogic.getBook(authorId, bookId));
    }

    /**
     * Asocia un Book existente a un Author
     *
     * @param authorId Identificador de la instancia de Author
     * @param bookId Identificador de la instancia de Book
     * @return Instancia de BookDTO que fue asociada a Author
     * @generated
     */
    @POST
    @Path("{authorId: \\d+}/books/{bookId: \\d+}")
    public BookDTO addBooks(@PathParam("authorId") Long authorId, @PathParam("bookId") Long bookId) {
        try {
            return BookConverter.fullEntity2DTO(authorLogic.addBook(authorId, bookId));
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Remplaza las instancias de Book asociadas a una instancia de Author
     *
     * @param authorId Identificador de la instancia de Author
     * @param books Colección de instancias de BookDTO a asociar a instancia de
     * Author
     * @return Nueva colección de BookDTO asociada a la instancia de Author
     * @generated
     */
    @PUT
    @Path("{authorId: \\d+}/books")
    public List<BookDTO> replaceBooks(@PathParam("authorId") Long authorId, List<BookDTO> books) {
        try {
            return BookConverter.listEntity2DTO(authorLogic.replaceBooks(BookConverter.listDTO2Entity(books), authorId));
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Desasocia un Book existente de un Author existente
     *
     * @param authorId Identificador de la instancia de Author
     * @param bookId Identificador de la instancia de Book
     * @generated
     */
    @DELETE
    @Path("{authorId: \\d+}/books/{bookId: \\d+}")
    public void removeBooks(@PathParam("authorId") Long authorId, @PathParam("bookId") Long bookId) {
        authorLogic.removeBook(authorId, bookId);
    }
}
