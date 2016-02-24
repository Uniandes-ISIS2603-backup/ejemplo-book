package co.edu.uniandes.csw.bookstore.persistence;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BookPersistence {

    @PersistenceContext(unitName = "BookStorePU")
    protected EntityManager em;

    public BookEntity find(Long id) {
        return em.find(BookEntity.class, id);
    }

    public List<BookEntity> findAll() {
        Query q = em.createQuery("select u from BookEntity u");
        return q.getResultList();
    }

    public BookEntity create(BookEntity entity) {
        em.persist(entity);
        return entity;
    }

    public BookEntity update(BookEntity entity) {
        return em.merge(entity);
    }

    public void delete(Long id) {
        BookEntity entity = em.find(BookEntity.class, id);
        em.remove(entity);
    }
}
