package co.edu.uniandes.csw.bookstore.logic;

import co.edu.uniandes.csw.bookstore.api.IBookLogic;
import co.edu.uniandes.csw.bookstore.ejbs.BookLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import co.edu.uniandes.csw.bookstore.entities.ReviewEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class BookLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IBookLogic bookLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<BookEntity> data = new ArrayList<BookEntity>();

    private List<AuthorEntity> authorsData = new ArrayList<>();

    private List<EditorialEntity> editorialData = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(BookEntity.class.getPackage())
                .addPackage(BookLogic.class.getPackage())
                .addPackage(IBookLogic.class.getPackage())
                .addPackage(BookPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void clearData() {
        em.createQuery("delete from ReviewEntity").executeUpdate();
        em.createQuery("delete from BookEntity").executeUpdate();
        em.createQuery("delete from AuthorEntity").executeUpdate();
        em.createQuery("delete from EditorialEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AuthorEntity authors = factory.manufacturePojo(AuthorEntity.class);
            System.out.println(authors.getBirthDate());
            em.persist(authors);
            authorsData.add(authors);
        }

        for (int i = 0; i < 3; i++) {
            EditorialEntity editorial = factory.manufacturePojo(EditorialEntity.class);
            em.persist(editorial);
            editorialData.add(editorial);
        }

        for (int i = 0; i < 3; i++) {
            BookEntity entity = factory.manufacturePojo(BookEntity.class);
            entity.setPublishDate(getMaxDate());
            System.out.println(entity.getPublishDate());
            for (ReviewEntity item : entity.getReviews()) {
                item.setBook(entity);
            }

            entity.getAuthors().add(authorsData.get(0));

            entity.setEditorial(editorialData.get(0));

            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createBookTest() {
        try {
            BookEntity entity = factory.manufacturePojo(BookEntity.class);
            entity.setPublishDate(getMaxDate());
            BookEntity created = bookLogic.createBook(entity);

            BookEntity result = em.find(BookEntity.class, created.getId());

            Assert.assertNotNull(result);
            Assert.assertEquals(entity.getId(), result.getId());
            Assert.assertEquals(entity.getName(), result.getName());
            Assert.assertEquals(entity.getDescription(), result.getDescription());
            Assert.assertEquals(entity.getIsbn(), result.getIsbn());
            Assert.assertEquals(entity.getImage(), result.getImage());
            Assert.assertEquals(entity.getPublishDate(), result.getPublishDate());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void getBooksTest() {
        List<BookEntity> resultList = bookLogic.getBooks();
        List<BookEntity> expectedList = em.createQuery("SELECT u from BookEntity u").getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (BookEntity expected : expectedList) {
            boolean found = false;
            for (BookEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getBookTest() {
        BookEntity result = bookLogic.getBook(data.get(0).getId());
        BookEntity expected = em.find(BookEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getIsbn(), result.getIsbn());
        Assert.assertEquals(expected.getImage(), result.getImage());
    }

    @Test
    public void deleteBookTest() {
        BookEntity entity = data.get(1);
        bookLogic.deleteBook(entity.getId());
        BookEntity deleted = em.find(BookEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateBookTest() {
        try {
            BookEntity entity = data.get(0);
            BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
            pojoEntity.setPublishDate(getMaxDate());

            pojoEntity.setId(entity.getId());

            bookLogic.updateBook(pojoEntity);

            BookEntity resp = em.find(BookEntity.class, entity.getId());

            Assert.assertEquals(pojoEntity.getId(), resp.getId());
            Assert.assertEquals(pojoEntity.getName(), resp.getName());
            Assert.assertEquals(pojoEntity.getDescription(), resp.getDescription());
            Assert.assertEquals(pojoEntity.getIsbn(), resp.getIsbn());
            Assert.assertEquals(pojoEntity.getImage(), resp.getImage());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void getAuthorTest() {
        BookEntity entity = data.get(0);
        AuthorEntity authorEntity = authorsData.get(0);
        AuthorEntity response = bookLogic.getAuthor(entity.getId(), authorEntity.getId());

        AuthorEntity expected = getBookAuthor(entity.getId(), authorEntity.getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(response);
        Assert.assertEquals(expected.getId(), response.getId());
        Assert.assertEquals(expected.getName(), response.getName());
        Assert.assertEquals(expected.getBirthDate(), response.getBirthDate());
    }

    @Test
    public void listAuthorsTest() {
        List<AuthorEntity> list = bookLogic.getAuthors(data.get(0).getId());
        BookEntity expected = em.find(BookEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getAuthors().size(), list.size());
    }

    @Test
    public void addAuthorsTest() {
        try {
            BookEntity entity = data.get(0);
            AuthorEntity authorEntity = authorsData.get(1);
            AuthorEntity response = bookLogic.addAuthor(authorEntity.getId(), entity.getId());

            AuthorEntity expected = getBookAuthor(entity.getId(), authorEntity.getId());

            Assert.assertNotNull(expected);
            Assert.assertNotNull(response);
            Assert.assertEquals(expected.getId(), response.getId());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void replaceAuthorsTest() {
        try {
            BookEntity entity = data.get(0);
            List<AuthorEntity> list = authorsData.subList(1, 3);
            bookLogic.replaceAuthors(list, entity.getId());

            BookEntity expected = em.find(BookEntity.class, entity.getId());

            Assert.assertNotNull(expected);
            Assert.assertFalse(expected.getAuthors().contains(authorsData.get(0)));
            Assert.assertTrue(expected.getAuthors().contains(authorsData.get(1)));
            Assert.assertTrue(expected.getAuthors().contains(authorsData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test(expected = NoResultException.class)
    public void removeAuthorsTest() {
        bookLogic.removeAuthor(authorsData.get(0).getId(), data.get(0).getId());
        getBookAuthor(data.get(0).getId(), authorsData.get(0).getId());
    }

    private Date getMaxDate() {
        Random r = new Random();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 9999);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
        return c.getTime();
    }

    private AuthorEntity getBookAuthor(Long bookId, Long authorId) {
        Query q = em.createQuery("Select DISTINCT a from BookEntity b join b.authors a where b.id = :bookId and a.id=:authorId");
        q.setParameter("bookId", bookId);
        q.setParameter("authorId", authorId);

        return (AuthorEntity) q.getSingleResult();
    }
}
