package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailPage extends BasePage {

    @FindBy(xpath = "//nav[@class='breadcrumb']")
    private WebElement pageTitle;

    @FindBy(xpath = "//a[@data-title='MLM - Mobile Launch Monitor']")
    private WebElement mobileLaunchMonitorOption;

    @FindBy(xpath = "//form[@class='add-to-cart-form']")
    private WebElement addToCartButton;

    @FindBy(xpath = "//span[@class='Acumin sticky-product-price js-sticky-price']")
    private WebElement productPrice;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductPageTitleCorrect() {
        return isElementDisplayed(pageTitle) &&
                pageTitle.getText().toUpperCase().contains("Mobıle Launch Monıtor".toUpperCase());
    }

    public void selectMobileLaunchMonitor() {
        clickElement(mobileLaunchMonitorOption);
    }

    public void clickAddToCart() {
        clickElement(addToCartButton);
    }

    public String getProductPrice() {
        return productPrice.getText().trim();
    }
}