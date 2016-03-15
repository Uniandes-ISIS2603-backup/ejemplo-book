package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.api.IEditorialLogic;
import co.edu.uniandes.csw.bookstore.converters.BookConverter;
import co.edu.uniandes.csw.bookstore.converters.EditorialConverter;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.dtos.EditorialDTO;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
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

@Path("editorials")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EditorialResource {
    
    private static final Logger logger = Logger.getLogger(EditorialResource.class.getName());

    @Inject
    private IEditorialLogic editorialLogic;

    /**
     * Obtiene la lista de los registros de Book.
     *
     * @return Colección de objetos de EditorialDTO cada uno con sus respectivos
     * Review
     * @generated
     */
    @GET
    public List<EditorialDTO> getEditorials() {
        return EditorialConverter.listEntity2DTO(editorialLogic.getEditorials());
    }

    /**
     * Obtiene los datos de una instancia de Book a partir de su ID.
     *
     * @param id Identificador de la instancia a consultar
     * @return Instancia de EditorialDTO con los datos del Book consultado y sus
     * Review
     * @generated
     */
    @GET
    @Path("{id: \\d+}")
    public EditorialDTO getEditorial(@PathParam("id") Long id) {
        try {
            return EditorialConverter.fullEntity2DTO(editorialLogic.getEditorial(id));
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, "La editorial no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
    }

    /**
     * Se encarga de crear un book en la base de datos.
     *
     * @param dto Objeto de EditorialDTO con los datos nuevos
     * @return Objeto de EditorialDTO con los datos nuevos y su ID.
     * @generated
     */
    @POST
    @StatusCreated
    public EditorialDTO createEditorial(EditorialDTO dto) {
        EditorialEntity entity = EditorialConverter.fullDTO2Entity(dto);
        return EditorialConverter.fullEntity2DTO(editorialLogic.createEditorial(entity));
    }

    /**
     * Actualiza la información de una instancia de Book.
     *
     * @param id Identificador de la instancia de Book a modificar
     * @param dto Instancia de EditorialDTO con los nuevos datos.
     * @return Instancia de EditorialDTO con los datos actualizados.
     * @generated
     */
    @PUT
    @Path("{id: \\d+}")
    public EditorialDTO updateEditorial(@PathParam("id") Long id, EditorialDTO dto) {
        EditorialEntity entity = EditorialConverter.fullDTO2Entity(dto);
        entity.setId(id);
        return EditorialConverter.fullEntity2DTO(editorialLogic.updateEditorial(entity));
    }

    /**
     * Elimina una instancia de Book de la base de datos.
     *
     * @param id Identificador de la instancia a eliminar.
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteEditorial(@PathParam("id") Long id) {
        editorialLogic.deleteEditorial(id);
    }

    /**
     * Obtiene una colección de instancias de BookDTO asociadas a una instancia
     * de Editorial
     *
     * @param editorialId Identificador de la instancia de Editorial
     * @return Colección de instancias de BookDTO asociadas a la instancia de
     * Editorial
     * @generated
     */
    @GET
    @Path("{editorialId: \\d+}/books")
    public List<BookDTO> listBooks(@PathParam("editorialId") Long editorialId) {
        return BookConverter.listEntity2DTO(editorialLogic.getBooks(editorialId));
    }

    /**
     * Obtiene una instancia de Book asociada a una instancia de Editorial
     *
     * @param editorialId Identificador de la instancia de Editorial
     * @param bookId Identificador de la instancia de Book
     * @generated
     */
    @GET
    @Path("{editorialId: \\d+}/books/{bookId: \\d+}")
    public BookDTO getBooks(@PathParam("editorialId") Long editorialId, @PathParam("bookId") Long bookId) {
        return BookConverter.fullEntity2DTO(editorialLogic.getBook(editorialId, bookId));
    }

    /**
     * Asocia un Book existente a un Editorial
     *
     * @param editorialId Identificador de la instancia de Editorial
     * @param bookId Identificador de la instancia de Book
     * @return Instancia de BookDTO que fue asociada a Editorial
     * @generated
     */
    @POST
    @Path("{editorialId: \\d+}/books/{bookId: \\d+}")
    public BookDTO addBooks(@PathParam("editorialId") Long editorialId, @PathParam("bookId") Long bookId) {
        return BookConverter.fullEntity2DTO(editorialLogic.addBook(editorialId, bookId));
    }

    /**
     * Remplaza las instancias de Book asociadas a una instancia de Editorial
     *
     * @param editorialId Identificador de la instancia de Editorial
     * @param books Colección de instancias de BookDTO a asociar a instancia de
     * Editorial
     * @return Nueva colección de BookDTO asociada a la instancia de Editorial
     * @generated
     */
    @PUT
    @Path("{editorialId: \\d+}/books")
    public List<BookDTO> replaceBooks(@PathParam("editorialId") Long editorialId, List<BookDTO> books) {
        return BookConverter.listEntity2DTO(editorialLogic.replaceBooks(BookConverter.listDTO2Entity(books), editorialId));
    }

    /**
     * Desasocia un Book existente de un Editorial existente
     *
     * @param editorialId Identificador de la instancia de Editorial
     * @param bookId Identificador de la instancia de Book
     * @generated
     */
    @DELETE
    @Path("{editorialId: \\d+}/books/{bookId: \\d+}")
    public void removeBooks(@PathParam("editorialId") Long editorialId, @PathParam("bookId") Long bookId) {
        editorialLogic.removeBook(editorialId, bookId);
    }
}
