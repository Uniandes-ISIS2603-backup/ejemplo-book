package co.edu.uniandes.csw.bookstore.selenium.pages;

import co.edu.uniandes.csw.bookstore.dtos.AuthorDTO;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("#/author")
public class AuthorPage {

    @FindBy(id = "create-author")
    private WebElement createButton;

    @FindBy(id = "refresh-author")
    private WebElement refreshButton;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "birthDate-datepicker")
    private WebElement birthDatePopUp;

    @FindByJQuery(value = "#birthDate + ul>li:nth-child(2)>span>button:first-child")
    private WebElement birthDateToday;

    @FindBy(id = "save-author")
    private WebElement saveButton;

    @FindBy(id = "0-edit-btn")
    private WebElement editFirstButton;

    @FindBy(id = "0-delete-btn")
    private WebElement deleteFirstButton;

    public void createAuthor(AuthorDTO dto) {
        createButton.click();
        saveAuthor(dto);
    }

    public void editFirstAuthor(AuthorDTO dto) {
        waitGui().until().element(editFirstButton).is().visible();
        editFirstButton.click();
        waitGui().until().element(saveButton).is().visible();
        saveAuthor(dto);
    }

    public void deleteFirstAuthor() {
        waitGui().until().element(deleteFirstButton).is().visible();
        deleteFirstButton.click();
        waitGui().until().element(deleteFirstButton).is().not().present();
    }

    private void saveAuthor(AuthorDTO dto) {
        waitGui().until().element(saveButton).is().visible();

        nameInput.clear();
        nameInput.sendKeys(dto.getName());
        birthDatePopUp.click();
        waitGui().until().element(birthDateToday).is().visible();
        birthDateToday.click();

        saveButton.click();
        waitGui().until().element(refreshButton).is().visible();
    }
}
