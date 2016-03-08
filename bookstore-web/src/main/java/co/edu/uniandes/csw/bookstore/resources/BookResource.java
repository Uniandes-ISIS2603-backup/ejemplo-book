package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.converters.BookConverter;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
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
import javax.ws.rs.core.MediaType;

@Path("books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger logger = Logger.getLogger(BookResource.class.getName());

    @Inject
    private IBookLogic bookLogic;

    @GET
    public List<BookDTO> getBook() {
        logger.info("Se ejecuta método getBooks");
        return BookConverter.listEntity2DTO(bookLogic.getBooks());
    }

    @GET
    @Path("{id: \\d+}")
    public BookDTO getBook(@PathParam("id") Long id) throws BusinessLogicException {
        logger.log(Level.INFO, "Se ejecuta método getBook con id={0}", id);
        return BookConverter.basicEntity2DTO(bookLogic.getBook(id));
    }

    @POST
    public BookDTO createBook(BookDTO dto) {
        logger.info("Se ejecuta método createBook");
        return BookConverter.basicEntity2DTO(bookLogic.createBook(BookConverter.basicDTO2Entity(dto)));
    }

    @PUT
    @Path("{id: \\d+}")
    public BookDTO updateBook(@PathParam("id") Long id, BookDTO dto) {
        logger.log(Level.INFO, "Se ejecuta método updateBook con id={0}", id);
        dto.setId(id);
        return BookConverter.basicEntity2DTO(bookLogic.updateBook(BookConverter.basicDTO2Entity(dto)));
    }

    @DELETE
    @Path("{id: \\d+}")
    public void deleteBook(@PathParam("id") Long id) {
        logger.log(Level.INFO, "Se ejecuta método deleteBook con id={0}", id);
        bookLogic.deleteBook(id);
    }
}
