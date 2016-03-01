package co.edu.uniandes.csw.bookstore.persistence;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AuthorPersistence {

    @PersistenceContext(unitName = "BookStorePU")
    protected EntityManager em;

    public AuthorEntity create(AuthorEntity entity) {
        em.persist(entity);
        return entity;
    }

    public AuthorEntity update(AuthorEntity entity) {
        return em.merge(entity);
    }

    public void delete(Long id) {
        AuthorEntity entity = em.find(AuthorEntity.class, id);
        em.remove(entity);
    }

    public AuthorEntity find(Long id) {
        return em.find(AuthorEntity.class, id);
    }

    public List<AuthorEntity> findAll() {
        Query q = em.createQuery("select u from AuthorEntity u");
        return q.getResultList();
    }

    public List<AuthorEntity> findByEditorial(Long editorialId) {
        Query q = em.createNamedQuery("AuthorEntity.findByEditorial");
        q.setParameter("editorial_id", editorialId);
        return q.getResultList();
    }
}
