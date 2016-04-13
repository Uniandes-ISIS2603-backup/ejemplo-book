package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.adapters.DateAdapter;
import co.edu.uniandes.csw.bookstore.converters.EditorialConverter;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.dtos.EditorialDTO;
import co.edu.uniandes.csw.bookstore.mappers.EJBExceptionMapper;
import co.edu.uniandes.csw.bookstore.providers.CreatedFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
public class EditorialResourceIT {

    private final int OK = Status.OK.getStatusCode();
    private final int CREATED = Status.CREATED.getStatusCode();
    private final int NO_CONTENT = Status.NO_CONTENT.getStatusCode();

    private final String editorialPath = "editorials";
    private final String booksPath = "books";

    private final static List<EditorialDTO> oraculo = new ArrayList<>();
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
                .addPackage(EditorialResource.class.getPackage())
                .addPackage(EditorialDTO.class.getPackage())
                .addPackage(EditorialConverter.class.getPackage())
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

    private WebTarget createWebTarget() {
        return ClientBuilder.newClient().target(deploymentURL.toString()).path(apiPath);
    }

    @BeforeClass
    public static void setUp() {
        insertData();
    }

    public static void insertData() {
        for (int i = 0; i < 5; i++) {

            EditorialDTO editorial = factory.manufacturePojo(EditorialDTO.class);
            editorial.setId(i + 1L);

            oraculo.add(editorial);

            BookDTO books = factory.manufacturePojo(BookDTO.class);
            books.setId(i + 1L);
            oraculoBooks.add(books);
        }
    }

    @Before
    public void setUpTest() {
        target = createWebTarget();
    }

    @Test
    @InSequence(1)
    public void createEditorialTest() throws IOException {
        EditorialDTO editorial = oraculo.get(0);

        Response response = target.path(editorialPath).request()
                .post(Entity.entity(editorial, MediaType.APPLICATION_JSON));

        EditorialDTO editorialTest = (EditorialDTO) response.readEntity(EditorialDTO.class);

        Assert.assertEquals(CREATED, response.getStatus());
        Assert.assertEquals(editorial.getName(), editorialTest.getName());
    }

    @Test
    @InSequence(2)
    public void getEditorialById() {

        EditorialDTO editorialTest = target.path(editorialPath)
                .path(oraculo.get(0).getId().toString())
                .request().get(EditorialDTO.class);
        Assert.assertEquals(editorialTest.getId(), oraculo.get(0).getId());
        Assert.assertEquals(editorialTest.getName(), oraculo.get(0).getName());
    }

    @Test
    @InSequence(3)
    public void listEditorialTest() throws IOException {

        Response response = target.path(editorialPath)
                .request().get();
        String listEditorial = response.readEntity(String.class);
        List<EditorialDTO> listEditorialTest = new ObjectMapper().readValue(listEditorial, List.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(1, listEditorialTest.size());
    }

    @Test
    @InSequence(4)
    public void updateEditorialTest() throws IOException {

        EditorialDTO editorial = oraculo.get(0);

        EditorialDTO editorialChanged = factory.manufacturePojo(EditorialDTO.class);
        editorial.setName(editorialChanged.getName());
        Response response = target.path(editorialPath).path(editorial.getId().toString())
                .request().put(Entity.entity(editorial, MediaType.APPLICATION_JSON));
        EditorialDTO editorialTest = (EditorialDTO) response.readEntity(EditorialDTO.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(editorial.getName(), editorialTest.getName());
    }

    @Test
    @InSequence(9)
    public void deleteEditorialTest() {

        EditorialDTO editorial = oraculo.get(0);
        Response response = target.path(editorialPath).path(editorial.getId().toString())
                .request().delete();
        Assert.assertEquals(NO_CONTENT, response.getStatus());
    }

    @Test
    @InSequence(5)
    public void addBooksTest() {

        BookDTO books = oraculoBooks.get(0);
        EditorialDTO editorial = oraculo.get(0);

        Response response = target.path("books")
                .request()
                .post(Entity.entity(books, MediaType.APPLICATION_JSON));

        BookDTO booksTest = (BookDTO) response.readEntity(BookDTO.class);
        Assert.assertEquals(books.getId(), booksTest.getId());
        Assert.assertEquals(books.getName(), booksTest.getName());
        Assert.assertEquals(books.getDescription(), booksTest.getDescription());
        Assert.assertEquals(books.getIsbn(), booksTest.getIsbn());
        Assert.assertEquals(books.getImage(), booksTest.getImage());
        Assert.assertEquals(CREATED, response.getStatus());

        response = target.path(editorialPath).path(editorial.getId().toString())
                .path(booksPath).path(books.getId().toString())
                .request()
                .post(Entity.entity(books, MediaType.APPLICATION_JSON));

        booksTest = (BookDTO) response.readEntity(BookDTO.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(books.getId(), booksTest.getId());
    }

    @Test
    @InSequence(6)
    public void listBooksTest() throws IOException {

        EditorialDTO editorial = oraculo.get(0);

        Response response = target.path(editorialPath)
                .path(editorial.getId().toString())
                .path(booksPath)
                .request().get();

        String booksList = response.readEntity(String.class);
        List<BookDTO> booksListTest = new ObjectMapper().readValue(booksList, List.class);
        Assert.assertEquals(OK, response.getStatus());
        Assert.assertEquals(1, booksListTest.size());
    }

    @Test
    @InSequence(7)
    public void getBooksTest() throws IOException {

        BookDTO books = oraculoBooks.get(0);
        EditorialDTO editorial = oraculo.get(0);

        BookDTO booksTest = target.path(editorialPath)
                .path(editorial.getId().toString()).path(booksPath)
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
        EditorialDTO editorial = oraculo.get(0);

        Response response = target.path(editorialPath).path(editorial.getId().toString())
                .path(booksPath).path(books.getId().toString())
                .request().delete();
        Assert.assertEquals(NO_CONTENT, response.getStatus());
    }
}
