/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejbs;

import co.edu.uniandes.csw.bookstore.api.IPrizeLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import co.edu.uniandes.csw.bookstore.persistence.PrizePersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author rcasalla
 */
public class PrizeLogic implements IPrizeLogic {

    private static final Logger logger = Logger.getLogger(PrizeLogic.class.getName());
    @Inject
    private BookPersistence persistence;

    @Inject
    private PrizePersistence prizePersistence;

    @Override
    public List<PrizeEntity> getPrizes(Long bookId) {
        BookEntity book = getBook(bookId);
        return book.getPrizes();
    }

    @Override
    public PrizeEntity getPrize(Long bookId, Long prizeId) {
        PrizeEntity prize = prizePersistence.find(bookId, prizeId);
        return prize;
    }

    @Override
    public PrizeEntity createPrize(Long bookId, PrizeEntity prize) {
        BookEntity book = getBook(bookId);
        prize = prizePersistence.create(prize);
        book.getPrizes().add(prize);
        return prize;
    }

    @Override
    public void deletePrize(Long bookId, Long prizeId) {
        PrizeEntity old = getPrize(bookId, prizeId);
        prizePersistence.delete(old.getId());
    }

    private BookEntity getBook(Long id) {
        logger.log(Level.INFO, "Inicia proceso de consultar libro con id={0}", id);
        BookEntity book = persistence.find(id);
        if (book == null) {
            logger.log(Level.SEVERE, "El libro con el id {0} no existe", id);
            throw new IllegalArgumentException("El libro solicitado no existe");
        }
        logger.log(Level.INFO, "Termina proceso de consultar libro con id={0}", id);
        return book;
    }
}
