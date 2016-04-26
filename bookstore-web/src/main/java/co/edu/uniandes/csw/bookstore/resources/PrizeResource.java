package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.api.IPrizeLogic;
import co.edu.uniandes.csw.bookstore.converters.PrizeConverter;
import co.edu.uniandes.csw.bookstore.dtos.PrizeDTO;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import java.util.List;
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

@Path("books/{bookId}/prizes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrizeResource {

    private static final Logger logger = Logger.getLogger(PrizeResource.class.getName());

    @Inject
    private IPrizeLogic prizeLogic;

    /**
     * Obtiene una colección de objetos de PrizeDTO asociados a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @return Colección de objetos de PrizeDTO en representación basic
     * @generated
     */
    @GET
    public List<PrizeDTO> getPrizes(@PathParam("bookId") Long bookId) {
        List<PrizeEntity> prizes = prizeLogic.getPrizes(bookId);
        return PrizeConverter.listEntity2DTO(prizes);
    }

    /**
     * Obtiene un objeto de Prize asociada a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @param prizeId Identificador del objeto de Prize
     * @generated
     */
    @GET
    @Path("{prizeId: \\d+}")
    public PrizeDTO getPrize(@PathParam("bookId") Long bookId, @PathParam("prizeId") Long prizeId) {
        PrizeEntity prize = prizeLogic.getPrize(bookId, prizeId);
        return PrizeConverter.fullEntity2DTO(prize);
    }

    /**
     * Asocia un Prize existente a un Book
     *
     * @param bookId Identificador del objeto de Book
     * @return Objeto de PrizeDTO en representación full que fue asociado a Book
     * @generated
     */
    @POST
    public PrizeDTO createPrize(@PathParam("bookId") Long bookId, PrizeDTO prize) {
        PrizeEntity entity = PrizeConverter.fullDTO2Entity(prize);
        entity = prizeLogic.createPrize(bookId, entity);
        return PrizeConverter.fullEntity2DTO(entity);
    }

    /**
     * Remplaza los objetos de Prize asociados a un objeto de Book
     *
     * @param bookId Identificador del objeto de Book
     * @param prizes Colección de objetos de PrizeDTO en representación minimum a asociar a objeto
     * de Book
     * @return Nueva colección de PrizeDTO en representación Basic
     * @generated
     */
    @PUT
    @Path("{prizeId: \\d+}")
    public PrizeDTO updatePrize(@PathParam("bookId") Long bookId, @PathParam("prizeId") Long prizeId, PrizeDTO prize) {
        PrizeEntity newPrize = PrizeConverter.fullDTO2Entity(prize);
        newPrize.setId(prizeId);
        newPrize = prizeLogic.updatePrize(bookId, newPrize);
        return PrizeConverter.fullEntity2DTO(newPrize);
    }

    /**
     * Desasocia un Prize existente de un Book existente
     *
     * @param bookId Identificador del objeto de Book
     * @param prizeId Identificador del objeto de Prize
     * @generated
     */
    @DELETE
    @Path("{prizeId: \\d+}")
    public void deletePrize(@PathParam("bookId") Long bookId, @PathParam("prizeId") Long prizeId) {
        prizeLogic.deletePrize(bookId, prizeId);
    }
}
