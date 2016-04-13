package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.adapters.DateAdapter;
import co.edu.uniandes.csw.bookstore.converters.AuthorConverter;
import co.edu.uniandes.csw.bookstore.dtos.AuthorDTO;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.mappers.EJBExceptionMapper;
import co.edu.uniandes.csw.bookstore.providers.CreatedFilter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class AuthorResourceIT {

    private final int OK = Status.OK.getStatusCode();
    private final int CREATED = Status.CREATED.getStatusCode();
    private final int NO_CONTENT = Status.NO_CONTENT.getStatusCode();

    private final String authorPath = "authors";
    private final static List<AuthorDTO> oraculo = new ArrayList<>();
    private final String booksPath = "books";
    private final static List<BookDTO> oraculoBooks = new ArrayList<>();

    private WebTarget target;
    private final String apiPath = "api";
    private static PodamFactory factory = new PodamFactoryImpl();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                // Se agrega la dependencia a la logica con el nombre groupid:artefactid:version (GAV)
                .addAsLibraries(Maven.resolver()
                        .resolve("co.edu.uniandes.csw.bookstore:bookstore-logic:1.0-SNAPSHOT")
                        .withTransitivity().asFile())
                // Se agregan los compilados de los paquetes de servicios
                .addPackage(AuthorResource.class.getPackage())
                .addPackage(AuthorDTO.class.getPackage())
                .addPackage(AuthorConverter.class.getPackage())
                .addPackage(EJBExceptionMapper.class.getPackage())
                .addPackage(DateAdapter.class.getPackage())
                .addPackage(CreatedFilter.class.getPackage())
                // El archivo que contiene la configuracion a la base de datos.
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // El archivo beans.xml es necesario para injeccion de dependencias.
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                // El archivo web.xml es necesario para el despliegue de los servlets
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Before
    public void setUpTest() {
        target = ClientBuilder.newClient().target(deploymentURL.toString()).path(apiPath);
    }

    @BeforeClass
    public static void setUp() {
        insertData();
    }

    public static void insertData() {
        for (int i = 0; i < 5; i++) {
            AuthorDTO author = factory.manufacturePojo(AuthorDTO.class);

            author.setId(i + 1L);

            oraculo.add(author);

            BookDTO books = factory.manufacturePojo(BookDTO.class);
            books.setPublishDate(getMaxDate());
            books.setId(i + 1L);
            oraculoBooks.add(books);
        }
    }

    @Test
    @InSequence(1)
    public void createAuthorTest() {
        AuthorDTO author = oraculo.get(0);
        Response response = target.path(authorPath).request()
                .post(Entity.entity(author, MediaType.APPLICATION_JSON));
        AuthorDTO authorTest = (AuthorDTO) response.readEntity(AuthorDTO.class);

        Assert.assertEquals(author.getName(), authorTest.getName());
        Assert.assertEquals(author.getBirthDate(), authorTest.getBirthDate());
        Assert.assertEquals(CREATED, response.getStatus());
    }

    @Test
    @InSequence(2)
    public void getAuthorById() {
        AuthorDTO authorTest = target.path(authorPath)
                .path(oraculo.get(0).getId().toString())
                .request().get(AuthorDTO.class);
        Assert.assertEquals(authorTest.getId(), oraculo.get(0).getId());
        Assert.assertEquals(authorTest.getName(), oraculo.get(0).getName());
        Assert.assertEquals(authorTest.getBirthDate(), oraculo.get(0).getBirthDate());
    }

    @Test
    @InSequence(3)
    public void listAuthorTest() {
        Response response = target.path(authorPath)
                .request().get();

        List<AuthorDTO> listAuthorTest = response.readEntity(new GenericType<List<AuthorDTO>>() {
        });
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(1, listAuthorTest.size());
    }

    @Test
    @InSequence(4)
    public void updateAuthorTest() {
        AuthorDTO author = oraculo.get(0);
        AuthorDTO authorChanged = factory.manufacturePojo(AuthorDTO.class);
        author.setName(authorChanged.getName());
        author.setBirthDate(authorChanged.getBirthDate());
        Response response = target.path(authorPath).path(author.getId().toString())
                .request().put(Entity.entity(author, MediaType.APPLICATION_JSON));
        AuthorDTO authorTest = (AuthorDTO) response.readEntity(AuthorDTO.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(author.getName(), authorTest.getName());
        Assert.assertEquals(author.getBirthDate(), authorTest.getBirthDate());
    }

    @Test
    @InSequence(9)
    public void deleteAuthorTest() {
        AuthorDTO author = oraculo.get(0);
        Response response = target.path(authorPath).path(author.getId().toString())
                .request().delete();
        Assert.assertEquals(NO_CONTENT, response.getStatus());
    }

    @Test
    @InSequence(5)
    public void addBooksTest() {
        BookDTO books = oraculoBooks.get(0);
        AuthorDTO author = oraculo.get(0);

        Response response = target.path("books")
                .request()
                .post(Entity.entity(books, MediaType.APPLICATION_JSON));

        Assert.assertEquals(response.getStatus(), CREATED);
        BookDTO booksTest = (BookDTO) response.readEntity(BookDTO.class);
        Assert.assertEquals(books.getId(), booksTest.getId());
        Assert.assertEquals(books.getName(), booksTest.getName());
        Assert.assertEquals(books.getDescription(), booksTest.getDescription());
        Assert.assertEquals(books.getIsbn(), booksTest.getIsbn());
        Assert.assertEquals(books.getImage(), booksTest.getImage());
        Assert.assertEquals(CREATED, response.getStatus());

        response = target.path(authorPath).path(author.getId().toString())
                .path(booksPath).path(books.getId().toString())
                .request()
                .post(Entity.entity(books, MediaType.APPLICATION_JSON));

        booksTest = (BookDTO) response.readEntity(BookDTO.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(books.getId(), booksTest.getId());
    }

    @Test
    @InSequence(6)
    public void listBooksTest() {
        AuthorDTO author = oraculo.get(0);

        Response response = target.path(authorPath)
                .path(author.getId().toString())
                .path(booksPath)
                .request().get();

        List<BookDTO> booksListTest = response.readEntity(new GenericType<List<BookDTO>>() {
        });
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(1, booksListTest.size());
    }

    @Test
    @InSequence(7)
    public void getBooksTest() {
        BookDTO books = oraculoBooks.get(0);
        AuthorDTO author = oraculo.get(0);

        BookDTO booksTest = target.path(authorPath)
                .path(author.getId().toString()).path(booksPath)
                .path(books.getId().toString())
                .request().get(BookDTO.class);

        Assert.assertEquals(books.getId(), booksTest.getId());
        Assert.assertEquals(books.getName(), booksTest.getName());
        Assert.assertEquals(books.getDescription(), booksTest.getDescription());
        Assert.assertEquals(books.getIsbn(), booksTest.getIsbn());
        Assert.assertEquals(books.getImage(), booksTest.getImage());
    }

    @Test
    @InSequence(8)
    public void removeBooksTest() {
        BookDTO books = oraculoBooks.get(0);
        AuthorDTO author = oraculo.get(0);

        Response response = target.path(authorPath).path(author.getId().toString())
                .path(booksPath).path(books.getId().toString())
                .request().delete();
        Assert.assertEquals(NO_CONTENT, response.getStatus());
    }

    private static Date getMaxDate() {
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
