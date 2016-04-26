/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.api.IPrizeLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.persistence.PrizePersistence;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 *
 * @author rcasalla
 */
public class PrizeLogic implements IPrizeLogic {

    private static final Logger logger = Logger.getLogger(PrizeLogic.class.getName());
    @Inject
    private IBookLogic bookLogic;

    @Inject
    private PrizePersistence prizePersistence;

    @Override
    public List<PrizeEntity> getPrizes(Long bookId) {
        BookEntity book = bookLogic.getBook(bookId);
        return book.getPrizes();
    }

    @Override
    public PrizeEntity getPrize(Long bookId, Long prizeId) {
        try {
            return prizePersistence.find(bookId, prizeId);
        }catch(NoResultException e){
            throw new IllegalArgumentException("El premio no existe");
        }
    }

    @Override
    public PrizeEntity createPrize(Long bookId, PrizeEntity prize) {
        BookEntity book = bookLogic.getBook(bookId);
        prize.setBook(book);
        prize = prizePersistence.create(prize);
        return prize;
    }

    @Override
    public PrizeEntity updatePrize(Long bookId, PrizeEntity prize) {
        BookEntity book = bookLogic.getBook(bookId);
        prize.setBook(book);
        // Se puede cambiar el book?
        return prizePersistence.update(prize);
    }

    @Override
    public void deletePrize(Long bookId, Long prizeId) {
        PrizeEntity old = getPrize(bookId, prizeId);
        prizePersistence.delete(old.getId());
    }
}
