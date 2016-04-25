package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.List;

public interface IPrizeLogic {

    public List<PrizeEntity> getPrizes(Long BookId);

    public PrizeEntity getPrize(Long BookId, Long PrizeId);

    public PrizeEntity createPrize(Long BookId, PrizeEntity entity) ;

 
    public void deletePrize(Long BookId, Long PrizeId);

    

}
