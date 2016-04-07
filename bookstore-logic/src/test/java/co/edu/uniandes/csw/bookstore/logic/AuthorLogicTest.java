package co.edu.uniandes.csw.bookstore.logic;

import co.edu.uniandes.csw.bookstore.api.IAuthorLogic;
import co.edu.uniandes.csw.bookstore.ejbs.AuthorLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.AuthorPersistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJBException;
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
public class AuthorLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IAuthorLogic authorLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<AuthorEntity> data = new ArrayList<AuthorEntity>();

    private List<BookEntity> booksData = new ArrayList<>();

    private static final String GET_BOOK_QUERY = "Select DISTINCT b from AuthorEntity a join a.books b where a.id=:authorId and b.id = :bookId";

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(AuthorEntity.class.getPackage())
                .addPackage(AuthorLogic.class.getPackage())
                .addPackage(IAuthorLogic.class.getPackage())
                .addPackage(AuthorPersistence.class.getPackage())
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
        em.createQuery("delete from BookEntity").executeUpdate();
        em.createQuery("delete from AuthorEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BookEntity books = factory.manufacturePojo(BookEntity.class);
            books.setPublishDate(getMaxDate());
            em.persist(books);
            booksData.add(books);
        }

        for (int i = 0; i < 3; i++) {
            AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);

            em.persist(entity);
            data.add(entity);

            booksData.get(0).getAuthors().add(entity);
        }
    }

    @Test
    public void createAuthorTest() {
        AuthorEntity expected = factory.manufacturePojo(AuthorEntity.class);
        AuthorEntity created = authorLogic.createAuthor(expected);

        AuthorEntity result = em.find(AuthorEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getBirthDate(), result.getBirthDate());
    }

    @Test
    public void getAuthorsTest() {
        List<AuthorEntity> resultList = authorLogic.getAuthors();
        List<AuthorEntity> expectedList = em.createQuery("SELECT u from AuthorEntity u").getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (AuthorEntity expected : data) {
            boolean found = false;
            for (AuthorEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getAuthorTest() {
        AuthorEntity result = authorLogic.getAuthor(data.get(0).getId());

        AuthorEntity expected = em.find(AuthorEntity.class, data.get(0).getId());

        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getBirthDate(), result.getBirthDate());
    }

    @Test
    public void deleteAuthorTest() {
        AuthorEntity entity = data.get(1);
        authorLogic.deleteAuthor(entity.getId());
        AuthorEntity expected = em.find(AuthorEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateAuthorTest() {
        AuthorEntity entity = data.get(0);
        AuthorEntity expected = factory.manufacturePojo(AuthorEntity.class);

        expected.setId(entity.getId());

        authorLogic.updateAuthor(expected);

        AuthorEntity resp = em.find(AuthorEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
        Assert.assertEquals(expected.getBirthDate(), resp.getBirthDate());
    }

    @Test
    public void getBookTest() {
        AuthorEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(0);
        BookEntity response = authorLogic.getBook(entity.getId(), bookEntity.getId());

        Query q = em.createQuery(GET_BOOK_QUERY);
        q.setParameter("authorId", entity.getId());
        q.setParameter("bookId", bookEntity.getId());

        BookEntity expected = (BookEntity) q.getSingleResult();

        Assert.assertNotNull(expected);
        Assert.assertNotNull(response);
        Assert.assertEquals(expected.getId(), response.getId());
        Assert.assertEquals(expected.getName(), response.getName());
        Assert.assertEquals(expected.getDescription(), response.getDescription());
        Assert.assertEquals(expected.getIsbn(), response.getIsbn());
        Assert.assertEquals(expected.getImage(), response.getImage());
    }

    @Test
    public void listBooksTest() {
        List<BookEntity> list = authorLogic.getBooks(data.get(0).getId());
        AuthorEntity expected = (AuthorEntity) em.find(AuthorEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getBooks().size(), list.size());
    }

    @Test
    public void addBooksTest() {
        try {
            AuthorEntity entity = data.get(0);
            BookEntity bookEntity = booksData.get(1);
            BookEntity response = authorLogic.addBook(bookEntity.getId(), entity.getId());

            Query q = em.createQuery(GET_BOOK_QUERY);
            q.setParameter("authorId", entity.getId());
            q.setParameter("bookId", bookEntity.getId());
            BookEntity expected = (BookEntity) q.getSingleResult();

            Assert.assertNotNull(expected);
            Assert.assertNotNull(response);
            Assert.assertEquals(expected.getId(), response.getId());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void replaceBooksTest() {
        try {
            AuthorEntity entity = data.get(0);
            List<BookEntity> list = booksData.subList(1, 3);
            authorLogic.replaceBooks(list, entity.getId());

            AuthorEntity expected = em.find(AuthorEntity.class, entity.getId());

            Assert.assertNotNull(expected);
            Assert.assertFalse(expected.getBooks().contains(booksData.get(0)));
            Assert.assertTrue(expected.getBooks().contains(booksData.get(1)));
            Assert.assertTrue(expected.getBooks().contains(booksData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test(expected = NoResultException.class)
    public void removeBooksTest() {
        authorLogic.removeBook(booksData.get(0).getId(), data.get(0).getId());
        try {
            authorLogic.getBook(data.get(0).getId(), booksData.get(0).getId());
            Assert.fail();
        } catch (EJBException e) {
        }

        Query q = em.createQuery(GET_BOOK_QUERY);
        q.setParameter("authorId", data.get(0).getId());
        q.setParameter("bookId", booksData.get(0).getId());
        q.getSingleResult();
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
}
