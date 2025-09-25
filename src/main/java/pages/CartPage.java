package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class CartPage extends BasePage {
    @FindBy(xpath = "(//*[contains(text(),'Your cart is currently empty.')])[1]")
    private WebElement emptyCartMessage;

    @FindBy(xpath = "//a[@class='CartButton ']")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//input[contains(@id,'cart_updates')]")
    private WebElement quantityInput;

    @FindBy(css = ".total-price, [class*='total']")
    private WebElement totalPrice;

    @FindBy(css = ".item-price, [class*='price']")
    private WebElement itemPrice;

    @FindBy(xpath = "//button[contains(@aria-label,'Increase item')]")
    private WebElement updateButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCartEmpty() {
        try {
            return emptyCartMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCartPageLoaded() {
        return driver.getCurrentUrl().contains("cart") ||
                driver.getCurrentUrl().contains("checkout");
    }

    public void increaseQuantityTo(int quantity) {
        // sayfada hata text olarak girince fiyat degişiyor.
        // ikinci hata eger text olarak ekledik sonra + basınca işemiyor
        //quantityInput.clear();
        //quantityInput.sendKeys(Keys.BACK_SPACE);
        //quantityInput.sendKeys(String.valueOf(quantity));
        if (updateButton.isDisplayed()) {
            clickElement(updateButton);
        }
        setTimer(2000);
    }

    public int getCartItemsCount() {
        return Integer.parseInt(cartItems.get(1).getText());
    }

    public String getTotalPrice() {
        return totalPrice.getText().trim();
    }

    public String getItemPrice() {
        return itemPrice.getText().trim();
    }

    public boolean verifyPriceMatch(String productPrice, String cartPrice) {
        String cleanProductPrice = productPrice.replaceAll("[^0-9.]", "");
        String cleanCartPrice = cartPrice.replaceAll("[^0-9.]", "");
        return cleanProductPrice.equals(cleanCartPrice);
    }

    public boolean verifyTotalPriceForQuantity(int quantity, String unitPrice) {
        try {
            double unitPriceValue = Double.parseDouble(unitPrice.replaceAll("[^0-9.]", ""));
            double expectedTotal = unitPriceValue * quantity;

            String actualTotalText = getTotalPrice();
            double actualTotal = Double.parseDouble(actualTotalText.replaceAll("[^0-9.]", ""));

            return Math.abs(expectedTotal - actualTotal) < 0.01;
        } catch (NumberFormatException e) {
            takeScreenshot("price_calculation_error");
            return false;
        }
    }
}
