package co.edu.uniandes.csw.bookstore.selenium.pages;

import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

@Location("#/book")
public class BookPage {

    @FindBy(id = "create-book")
    private WebElement createButton;

    @FindBy(id = "refresh-book")
    private WebElement refreshButton;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "description")
    private WebElement descriptionInput;

    @FindBy(id = "isbn")
    private WebElement isbnInput;

    @FindBy(id = "image")
    private WebElement imageInput;

    @FindBy(id = "editorial")
    private Select editorialInput;

    @FindBy(id = "publishDate-datepicker")
    private WebElement publishDatePopUp;

    @FindByJQuery(value = "#publishDate + ul>li:nth-child(2)>span>button:first-child")
    private WebElement publishDateToday;

    @FindBy(id = "save-book")
    private WebElement saveButton;

    @FindBy(id = "0-edit-btn")
    private WebElement editFirstButton;

    @FindBy(id = "0-delete-btn")
    private WebElement deleteFirstButton;

    public void createBook(BookDTO dto) {
        createButton.click();
        saveBook(dto);
    }

    public void editFirstBook(BookDTO dto) {
        waitGui().until().element(editFirstButton).is().visible();
        editFirstButton.click();
        waitAjax().until().element(saveButton).is().visible();
        saveBook(dto);
    }

    public void deleteFirstBook() {
        waitGui().until().element(deleteFirstButton).is().visible();
        deleteFirstButton.click();
        waitAjax().until().element(deleteFirstButton).is().not().present();
    }

    private void saveBook(BookDTO dto) {
        waitGui().until().element(saveButton).is().visible();
        nameInput.clear();
        descriptionInput.clear();
        isbnInput.clear();
        imageInput.clear();

        nameInput.sendKeys(dto.getName());
        descriptionInput.sendKeys(dto.getDescription());
        isbnInput.sendKeys(dto.getIsbn());
        imageInput.sendKeys(dto.getImage());
        publishDatePopUp.click();
        waitGui().until().element(publishDateToday).is().visible();
        publishDateToday.click();

        saveButton.click();
        waitAjax().until().element(refreshButton).is().visible();
    }
}
