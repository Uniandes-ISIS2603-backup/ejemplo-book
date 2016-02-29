package co.edu.uniandes.csw.bookstore.providers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        return Response.status(ex.getResponse().getStatus())
                .entity(getInitCause(ex).getLocalizedMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    private Throwable getInitCause(Throwable e) {
        if (e.getCause() != null) {
            return getInitCause(e.getCause());
        } else {
            return e;
        }
    }
}
