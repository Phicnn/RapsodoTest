package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class GolfProductsPage extends BasePage{
    @FindBy(xpath = "(//a[contains(text(),' MLM') and contains(@href,'/products/mobile-launch-monitor')])[3]")
    private WebElement mlmProduct;

    //gereksiz çıkarılacak
    //@FindBy(xpath = "//a[contains(text(),'Shop MLM') or contains(@href,'mlm')]")
    //private WebElement shopMLMButton;

    public GolfProductsPage(WebDriver driver) {
        super(driver);
    }

    public void clickMLMProduct() {
        clickElement(mlmProduct);
    }

    /*public void clickShopMLM() {
        clickElement(shopMLMButton);}
     */
}
