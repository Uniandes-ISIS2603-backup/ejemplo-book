package co.edu.uniandes.csw.bookstore.api;

import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import java.util.List;

public interface IPrizeLogic {

    public List<PrizeEntity> getPrizes(Long BookId);

    public PrizeEntity getPrize(Long BookId, Long PrizeId);

    public PrizeEntity createPrize(Long BookId, PrizeEntity entity);

    public PrizeEntity updatePrize(Long bookId, PrizeEntity prize);

    public void deletePrize(Long BookId, Long PrizeId);

}
