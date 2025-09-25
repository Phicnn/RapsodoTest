package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    @FindBy(xpath = "(//a[contains(@class,'CartButton')])[2]")
    private WebElement cartIcon;

    @FindBy(xpath = "//li[contains(@class, 'TabLink')]//a[contains(@href, '/pages/golf')]")
    private WebElement golfMenu;

    @FindBy(xpath = "//li[contains(text(), 'PRODUCTS')]")
    private WebElement productsMenu;

    public HomePage(WebDriver driver) {
        super(driver);
    }
    public void clickCartIcon() {
        clickElement(cartIcon);
    }

    public void hoverOverGolfMenu() {
        hoverOverElement(golfMenu);
    }

    public void clickProductsMenu() {
        clickElement(productsMenu);
    }

}
