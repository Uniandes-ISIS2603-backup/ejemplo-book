package co.edu.uniandes.csw.bookstore.services;

import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import java.util.List;
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
public class BookService {

    @GET
    public List<BookDTO> getBook() {
        return null; //Implementar
    }

    @GET
    @Path("{id: \\d+}")
    public BookDTO getBook(@PathParam("id") Long id) {
        return null; //Implementar
    }

    @POST
    public BookDTO createBook(BookDTO dto) {
        return null; //Implementar
    }

    @PUT
    @Path("{id: \\d+}")
    public BookDTO updateBook(@PathParam("id") Long id, BookDTO dto) {
        return null; //Implementar
    }

    @DELETE
    @Path("{id: \\d+}")
    public void deleteBook(@PathParam("id") Long id) {

    }
}
