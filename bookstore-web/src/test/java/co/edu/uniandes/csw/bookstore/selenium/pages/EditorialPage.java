package co.edu.uniandes.csw.bookstore.selenium.pages;

import co.edu.uniandes.csw.bookstore.dtos.EditorialDTO;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("#/editorial")
public class EditorialPage {

    @FindBy(id = "create-editorial")
    private WebElement createButton;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "save-editorial")
    private WebElement saveButton;

    public void createEditorial(EditorialDTO dto) {
        createButton.click();
        waitGui().until().element(By.id("name")).is().visible();
        nameInput.sendKeys(dto.getName());
        saveButton.click();
    }
}
