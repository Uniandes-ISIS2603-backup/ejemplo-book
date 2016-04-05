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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
        AuthorEntity result = authorLogic.createAuthor(entity);

        AuthorEntity resp = em.find(AuthorEntity.class, result.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(resp);
        Assert.assertEquals(entity.getId(), resp.getId());
        Assert.assertEquals(entity.getName(), resp.getName());
        Assert.assertEquals(entity.getBirthDate(), resp.getBirthDate());
    }

    @Test
    public void getAuthorsTest() {
        List<AuthorEntity> list = authorLogic.getAuthors();
        Assert.assertEquals(data.size(), list.size());
        for (AuthorEntity entity : list) {
            boolean found = false;
            for (AuthorEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getAuthorTest() {
        try {
            AuthorEntity entity = data.get(0);
            AuthorEntity resultEntity = authorLogic.getAuthor(entity.getId());
            Assert.assertNotNull(resultEntity);
            Assert.assertEquals(entity.getId(), resultEntity.getId());
            Assert.assertEquals(entity.getName(), resultEntity.getName());
            Assert.assertEquals(entity.getBirthDate(), resultEntity.getBirthDate());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteAuthorTest() {
        AuthorEntity entity = data.get(1);
        authorLogic.deleteAuthor(entity.getId());
        AuthorEntity deleted = em.find(AuthorEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateAuthorTest() {
        AuthorEntity entity = data.get(0);
        AuthorEntity pojoEntity = factory.manufacturePojo(AuthorEntity.class);

        pojoEntity.setId(entity.getId());

        authorLogic.updateAuthor(pojoEntity);

        AuthorEntity resp = em.find(AuthorEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getId(), resp.getId());
        Assert.assertEquals(pojoEntity.getName(), resp.getName());
        Assert.assertEquals(pojoEntity.getBirthDate(), resp.getBirthDate());
    }

    @Test
    public void getBookTest() {
        AuthorEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(0);
        BookEntity response = authorLogic.getBook(entity.getId(), bookEntity.getId());

        Assert.assertEquals(bookEntity.getId(), response.getId());
        Assert.assertEquals(bookEntity.getName(), response.getName());
        Assert.assertEquals(bookEntity.getDescription(), response.getDescription());
        Assert.assertEquals(bookEntity.getIsbn(), response.getIsbn());
        Assert.assertEquals(bookEntity.getImage(), response.getImage());
    }

    @Test
    public void listBooksTest() {
        List<BookEntity> list = authorLogic.getBooks(data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void addBooksTest() {
        try {
            AuthorEntity entity = data.get(0);
            BookEntity bookEntity = booksData.get(1);
            BookEntity response = authorLogic.addBook(bookEntity.getId(), entity.getId());

            Assert.assertNotNull(response);
            Assert.assertEquals(bookEntity.getId(), response.getId());
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

            entity = authorLogic.getAuthor(entity.getId());
            Assert.assertFalse(entity.getBooks().contains(booksData.get(0)));
            Assert.assertTrue(entity.getBooks().contains(booksData.get(1)));
            Assert.assertTrue(entity.getBooks().contains(booksData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removeBooksTest() {
        authorLogic.removeBook(booksData.get(0).getId(), data.get(0).getId());
        BookEntity response = authorLogic.getBook(data.get(0).getId(), booksData.get(0).getId());
        Assert.assertNull(response);
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
