package co.edu.uniandes.csw.bookstore.logic;

import co.edu.uniandes.csw.bookstore.api.IEditorialLogic;
import co.edu.uniandes.csw.bookstore.ejbs.EditorialLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.EditorialPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), entity.getId());
        Assert.assertEquals(result.getName(), entity.getName());
    }

    @Test
    public void getEditorialsTest() {
        List<EditorialEntity> list = editorialLogic.getEditorials();
        Assert.assertEquals(data.size(), list.size());
        for (EditorialEntity entity : list) {
            boolean found = false;
            for (EditorialEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getEditorialTest() {
        try {
            EditorialEntity entity = data.get(0);
            EditorialEntity resultEntity = editorialLogic.getEditorial(entity.getId());
            Assert.assertNotNull(resultEntity);
            Assert.assertEquals(entity.getId(), resultEntity.getId());
            Assert.assertEquals(entity.getName(), resultEntity.getName());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
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
        EditorialEntity pojoEntity = factory.manufacturePojo(EditorialEntity.class);

        pojoEntity.setId(entity.getId());

        editorialLogic.updateEditorial(pojoEntity);

        EditorialEntity resp = em.find(EditorialEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getId(), resp.getId());
        Assert.assertEquals(pojoEntity.getName(), resp.getName());
    }

    @Test
    public void getBookTest() {
        EditorialEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(0);
        BookEntity response = editorialLogic.getBook(entity.getId(), bookEntity.getId());

        Assert.assertEquals(bookEntity.getId(), response.getId());
        Assert.assertEquals(bookEntity.getName(), response.getName());
        Assert.assertEquals(bookEntity.getDescription(), response.getDescription());
        Assert.assertEquals(bookEntity.getIsbn(), response.getIsbn());
        Assert.assertEquals(bookEntity.getImage(), response.getImage());
    }

    @Test
    public void listBooksTest() {
        List<BookEntity> list = editorialLogic.getBooks(data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void addBooksTest() {
        EditorialEntity entity = data.get(0);
        BookEntity bookEntity = booksData.get(1);
        BookEntity response = editorialLogic.addBook(bookEntity.getId(), entity.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(bookEntity.getId(), response.getId());
    }

    @Test
    public void replaceBooksTest() {
        try {
            EditorialEntity entity = data.get(0);
            List<BookEntity> list = booksData.subList(1, 3);
            editorialLogic.replaceBooks(list, entity.getId());

            entity = editorialLogic.getEditorial(entity.getId());
            Assert.assertFalse(entity.getBooks().contains(booksData.get(0)));
            Assert.assertTrue(entity.getBooks().contains(booksData.get(1)));
            Assert.assertTrue(entity.getBooks().contains(booksData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removeBooksTest() {
        editorialLogic.removeBook(booksData.get(0).getId(), data.get(0).getId());
        BookEntity response = editorialLogic.getBook(data.get(0).getId(), booksData.get(0).getId());
        Assert.assertNull(response);
    }
}
