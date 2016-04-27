
import co.edu.uniandes.csw.bookstore.api.IPrizeLogic;
import co.edu.uniandes.csw.bookstore.ejbs.PrizeLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.PrizePersistence;
import java.util.ArrayList;
import java.util.List;
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
public class PrizeLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IPrizeLogic prizeLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<PrizeEntity> data = new ArrayList<>();

    private List<BookEntity> booksData = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(PrizeEntity.class.getPackage())
                .addPackage(PrizePersistence.class.getPackage())
                .addPackage(PrizeLogic.class.getPackage())
                .addPackage(IPrizeLogic.class.getPackage())
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
        em.createQuery("delete from PrizeEntity").executeUpdate();
        em.createQuery("delete from BookEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BookEntity books = factory.manufacturePojo(BookEntity.class);
            em.persist(books);
            booksData.add(books);
        }

        for (int i = 0; i < 3; i++) {
            PrizeEntity entity = factory.manufacturePojo(PrizeEntity.class);
            entity.setBook(booksData.get(0));
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createPrizeTest() {
        PrizeEntity entity = factory.manufacturePojo(PrizeEntity.class);
        PrizeEntity created = prizeLogic.createPrize(booksData.get(0).getId(), entity);

        PrizeEntity result = em.find(PrizeEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertEquals(entity.getId(), result.getId());
        Assert.assertEquals(entity.getName(), result.getName());
        Assert.assertEquals(entity.getOrganization(), result.getOrganization());
        Assert.assertEquals(entity.getDate(), result.getDate());
    }

    @Test
    public void getPrizesTest() {
        Long bookId = booksData.get(0).getId();
        List<PrizeEntity> resultList = prizeLogic.getPrizes(bookId);
        List<PrizeEntity> expectedList = em.createQuery("SELECT u from PrizeEntity u join u.book b where b.id=:bookId")
                .setParameter("bookId", bookId)
                .getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (PrizeEntity expected : expectedList) {
            boolean found = false;
            for (PrizeEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getPrizeTest() {
        Long bookId = booksData.get(0).getId();
        Long prizeId = data.get(0).getId();
        PrizeEntity result = prizeLogic.getPrize(bookId, prizeId);
        PrizeEntity expected = em.find(PrizeEntity.class, prizeId);

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getOrganization(), result.getOrganization());
        Assert.assertEquals(expected.getDate(), result.getDate());
    }

    @Test
    public void deletePrizeTest() {
        Long bookId = booksData.get(0).getId();
        PrizeEntity entity = data.get(1);
        prizeLogic.deletePrize(bookId, entity.getId());
        PrizeEntity deleted = em.find(PrizeEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updatePrizeTest() {
        PrizeEntity entity = data.get(0);
        Long bookId = entity.getBook().getId();
        PrizeEntity pojoEntity = factory.manufacturePojo(PrizeEntity.class);

        pojoEntity.setId(entity.getId());

        prizeLogic.updatePrize(bookId, pojoEntity);

        PrizeEntity resp = em.find(PrizeEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getId(), resp.getId());
        Assert.assertEquals(pojoEntity.getName(), resp.getName());
        Assert.assertEquals(pojoEntity.getOrganization(), resp.getOrganization());
        Assert.assertEquals(pojoEntity.getDate(), resp.getDate());
    }
}
