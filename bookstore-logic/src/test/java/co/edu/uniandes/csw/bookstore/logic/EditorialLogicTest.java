package co.edu.uniandes.csw.bookstore.logic;

import co.edu.uniandes.csw.bookstore.api.IEditorialLogic;
import co.edu.uniandes.csw.bookstore.ejbs.EditorialLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import co.edu.uniandes.csw.bookstore.persistence.EditorialPersistence;
import java.util.ArrayList;
import java.util.List;
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
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class EditorialLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IEditorialLogic editorialLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<EditorialEntity> data = new ArrayList<EditorialEntity>();

    private List<BookEntity> booksData = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EditorialEntity.class.getPackage())
                .addPackage(EditorialLogic.class.getPackage())
                .addPackage(IEditorialLogic.class.getPackage())
                .addPackage(EditorialPersistence.class.getPackage())
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
        em.createQuery("delete from EditorialEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BookEntity books = factory.manufacturePojo(BookEntity.class);
            em.persist(books);
            booksData.add(books);
        }

        for (int i = 0; i < 3; i++) {
            EditorialEntity entity = factory.manufacturePojo(EditorialEntity.class);

            em.persist(entity);
            data.add(entity);

            if (i == 0) {
                booksData.get(i).setEditorial(entity);
            }
        }
    }

    @Test
    public void createEditorialTest() {
        EditorialEntity entity = factory.manufacturePojo(EditorialEntity.class);
        EditorialEntity result = editorialLogic.createEditorial(entity);

        EditorialEntity resp = em.find(EditorialEntity.class, entity.getId());

        Assert.assertNotNull(result);
        Assert.assertEquals(entity.getId(), resp.getId());
        Assert.assertEquals(entity.getName(), resp.getName());
    }

    @Test
    public void getEditorialsTest() {
        List<EditorialEntity> resultList = editorialLogic.getEditorials();
        List<EditorialEntity> expectedList = em.createQuery("SELECT u from EditorialEntity u").getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (EditorialEntity expected : expectedList) {
            boolean found = false;
            for (EditorialEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getEditorialTest() {
        EditorialEntity result = editorialLogic.getEditorial(data.get(0).getId());

        EditorialEntity expected = em.find(EditorialEntity.class, data.get(0).getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
    }

    @Test
    public void deleteEditorialTest() {
        EditorialEntity entity = data.get(1);
        editorialLogic.deleteEditorial(entity.getId());
        EditorialEntity deleted = em.find(EditorialEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateEditorialTest() {
        EditorialEntity entity = data.get(0);
        EditorialEntity expected = factory.manufacturePojo(EditorialEntity.class);

        expected.setId(entity.getId());

        editorialLogic.updateEditorial(expected);

        EditorialEntity resp = em.find(EditorialEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
    }

    @Test
    public void getBookTest() {
        EditorialEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(0);
        BookEntity response = editorialLogic.getBook(entity.getId(), bookEntity.getId());

        BookEntity expected = getEditorialBook(entity.getId(), bookEntity.getId());

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
        List<BookEntity> list = editorialLogic.getBooks(data.get(0).getId());
        EditorialEntity expected = em.find(EditorialEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void addBooksTest() {
        EditorialEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(1);
        BookEntity response = editorialLogic.addBook(bookEntity.getId(), entity.getId());

        BookEntity expected = getEditorialBook(entity.getId(), bookEntity.getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(response);
        Assert.assertEquals(expected.getId(), response.getId());
    }

    @Test
    public void replaceBooksTest() {
        EditorialEntity entity = data.get(0);
        List<BookEntity> list = booksData.subList(1, 3);
        editorialLogic.replaceBooks(list, entity.getId());

        EditorialEntity expected = em.find(EditorialEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertFalse(expected.getBooks().contains(booksData.get(0)));
        Assert.assertTrue(expected.getBooks().contains(booksData.get(1)));
        Assert.assertTrue(expected.getBooks().contains(booksData.get(2)));
    }

    @Test(expected = NoResultException.class)
    public void removeBooksTest() {
        editorialLogic.removeBook(booksData.get(0).getId(), data.get(0).getId());
        getEditorialBook(data.get(0).getId(), booksData.get(0).getId());
    }

    private BookEntity getEditorialBook(Long editorialId, Long bookId) {
        Query q = em.createQuery("Select DISTINCT b from EditorialEntity e join e.books b where e.id=:editorialId and b.id = :bookId");
        q.setParameter("bookId", bookId);
        q.setParameter("editorialId", editorialId);

        return (BookEntity) q.getSingleResult();
    }
}
