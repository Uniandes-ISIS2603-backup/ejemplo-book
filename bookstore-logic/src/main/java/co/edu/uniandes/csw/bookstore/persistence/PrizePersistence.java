/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.persistence;

import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class PrizePersistence {
    
    private static final Logger logger = Logger.getLogger(PrizePersistence.class.getName());

    @PersistenceContext(unitName = "BookStorePU")
    protected EntityManager em;

    public PrizeEntity find(Long bookId, Long prizeId) {
        TypedQuery<PrizeEntity> q = em.createQuery("select p from PrizeEntity p where (p.book.id = :bookId) and (p.id = :prizeId)", PrizeEntity.class);
        q.setParameter("bookId", bookId);
        q.setParameter("prizeId", prizeId);
        return q.getSingleResult();
    }


    public PrizeEntity create(PrizeEntity entity) {
        logger.info("Creando un premio nuevo");
        em.persist(entity);
        logger.info("Premio creado");
        return entity;
    }

    public PrizeEntity update(PrizeEntity entity) {
        logger.log(Level.INFO, "Actualizando premio con id={0}", entity.getId());
        return em.merge(entity);
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Borrando premio con id={0}", id);
        PrizeEntity entity = em.find(PrizeEntity.class, id);
        em.remove(entity);
    }
}
