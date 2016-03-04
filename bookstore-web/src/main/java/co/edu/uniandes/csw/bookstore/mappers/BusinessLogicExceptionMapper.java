package co.edu.uniandes.csw.bookstore.mappers;

import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Convertidor de Excepciones BusinessLogicException a mensajes REST.
 */
@Provider
public class BusinessLogicExceptionMapper implements ExceptionMapper<BusinessLogicException> {

    /**
     * Generador de una respuesta a partir de una excepción
     *
     * @param exception excecpión a convertir a una respuesta REST
     */
    @Override
    public Response toResponse(BusinessLogicException exception) {
        return Response
                .status(Response.Status.NOT_FOUND) // estado HTTP 404
                .entity(exception.getMessage()) // mensaje adicional
                .type("text/plain")
                .build();
    }
}
