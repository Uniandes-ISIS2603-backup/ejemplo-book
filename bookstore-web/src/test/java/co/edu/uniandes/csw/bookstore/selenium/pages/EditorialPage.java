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

    @FindBy(id = "0-edit-btn")
    private WebElement editFirstButton;

    @FindBy(id = "0-delete-btn")
    private WebElement deleteFirstButton;

    public void createEditorial(EditorialDTO dto) {
        createButton.click();
        saveEditorial(dto);
    }

    public void editFirstEditorial(EditorialDTO dto){
        waitGui().until().element(editFirstButton).is().visible();
        editFirstButton.click();
        waitGui().until().element(saveButton).is().visible();
        nameInput.clear();
        saveEditorial(dto);
    }

    public void deleteFirstEditorial(){
        waitGui().until().element(deleteFirstButton).is().visible();
        deleteFirstButton.click();
        waitGui().until().element(deleteFirstButton).is().not().present();
    }

    private void saveEditorial(EditorialDTO dto){
        waitGui().until().element(saveButton).is().visible();
        nameInput.sendKeys(dto.getName());
        saveButton.click();
    }
}
