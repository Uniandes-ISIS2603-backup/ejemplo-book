package co.edu.uniandes.csw.bookstore.selenium;

import co.edu.uniandes.csw.bookstore.adapters.DateAdapter;
import co.edu.uniandes.csw.bookstore.converters.BookConverter;
import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.dtos.EditorialDTO;
import co.edu.uniandes.csw.bookstore.mappers.EJBExceptionMapper;
import co.edu.uniandes.csw.bookstore.providers.CreatedFilter;
import co.edu.uniandes.csw.bookstore.resources.BookResource;
import co.edu.uniandes.csw.bookstore.selenium.pages.BookPage;
import co.edu.uniandes.csw.bookstore.selenium.pages.EditorialPage;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class BookIT {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                // Se agrega la dependencia a la logica con el nombre groupid:artefactid:version (GAV)
                .addAsLibraries(Maven.resolver()
                        .resolve("co.edu.uniandes.csw.bookstore:bookstore-logic:1.0-SNAPSHOT")
                        .withTransitivity().asFile())
                // Se agregan los compilados de los paquetes de servicios
                .addPackage(BookResource.class.getPackage())
                .addPackage(BookDTO.class.getPackage())
                .addPackage(BookConverter.class.getPackage())
                .addPackage(EJBExceptionMapper.class.getPackage())
                .addPackage(DateAdapter.class.getPackage())
                .addPackage(CreatedFilter.class.getPackage())
                // El archivo que contiene la configuracion a la base de datos.
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // El archivo beans.xml es necesario para injeccion de dependencias.
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                // El archivo web.xml es necesario para el despliegue de los servlets
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory("src/main/webapp").as(GenericArchive.class), "/");
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentURL;

    private static PodamFactory factory = new PodamFactoryImpl();

    @Before
    public void loadPage() throws InterruptedException {
        browser.get(deploymentURL.toExternalForm());
        Thread.sleep(5000);
    }

    @Test
    @InSequence(1)
    public void createEditorial(@InitialPage EditorialPage editorialPage) {
        EditorialDTO editorial = factory.manufacturePojo(EditorialDTO.class);
        editorialPage.createEditorial(editorial);
        WebElement name1 = browser.findElement(By.id("0-name"));
        Assert.assertTrue(name1.isDisplayed());
        Assert.assertEquals(editorial.getName(), name1.getText());
    }


    @Test
    @InSequence(2)
    public void createBook(@InitialPage BookPage bookPage) {
        BookDTO book = factory.manufacturePojo(BookDTO.class);
        bookPage.createBook(book);

        WebElement name1 = browser.findElement(By.id("0-name"));

        Assert.assertTrue(name1.isDisplayed());
        Assert.assertEquals(book.getName(), name1.getText());
    }

}
