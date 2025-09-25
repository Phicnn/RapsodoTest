package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.CartPage;
import pages.HomePage;
import pages.GolfProductsPage;
import pages.ProductDetailPage;
import utils.ConfigReader;
import utils.Driver;
import utils.TestListener;

import java.util.Properties;

@Listeners(TestListener.class)
public class RapsodoTestAutomation {

    private WebDriver driver;
    private HomePage homePage;
    private Properties properties;
    private GolfProductsPage golfProductsPage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        TestListener.logInfo("Test starting - Browser: " + browser);

        this.driver = Driver.getDriver(browser);
        this.homePage = new HomePage(driver);
        this.golfProductsPage = new GolfProductsPage(driver);
        this.productDetailPage = new ProductDetailPage(driver);
        this.cartPage = new CartPage(driver);
        this.properties = ConfigReader.getProperties();

        TestListener.logInfo("Page objects created successfully");
    }

    @Test(priority = 1)
    public void test_AddingProductToTheCart() {
        TestListener.logInfo("=== Test Step 1: Homepage Verification ===");

        //  "https://rapsodo.com" adresine doğrula
        TestListener.logInfo("Navigating to homepage: https://rapsodo.com");
        driver.get(properties.getProperty("rapsodo.url"));

        Assert.assertTrue(driver.getCurrentUrl().contains("rapsodo.com"),
                "Rapsodo home page is not opened correctly");
        TestListener.logPass("Homepage opened successfully");

        // Sepet ikonuna tıkla ve sepetin boş olduğunu doğrula
        TestListener.logInfo("Clicking on cart icon");
        homePage.clickCartIcon();

        Assert.assertTrue(cartPage.isCartEmpty(), "Your cart is currently empty.");
        TestListener.logPass("Cart is verified to be empty");

        // Golf menüsüne tıkla ve MLM ürününü seç
        TestListener.logInfo("Hovering over Golf menu");
        homePage.hoverOverGolfMenu();

        TestListener.logInfo("Clicking on Products menu");
        homePage.clickProductsMenu();

        TestListener.logInfo("Clicking on MLM product");
        golfProductsPage.clickMLMProduct();

        //gereksiz
        //TestListener.logInfo("Clicking on Shop MLM button");
        //golfProductsPage.clickShopMLM();

        // Sayfa başlığın
        Assert.assertTrue(productDetailPage.isProductPageTitleCorrect(),
                "MLM product page title is incorrect");
        TestListener.logPass("MLM product page title is correct");

        // Mobile Launch Monitor seçeneğini seç
        TestListener.logInfo("Selecting Mobile Launch Monitor option");
        productDetailPage.selectMobileLaunchMonitor();

        // Sepete ekle
        TestListener.logInfo("Getting product price");
        String productPrice = productDetailPage.getProductPrice();
        TestListener.logInfo("Product price: " + productPrice);

        TestListener.logInfo("Clicking Add to Cart button");
        productDetailPage.clickAddToCart();

        // Sepet sayfasına
        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "Not redirected to cart page");
        TestListener.logPass("Successfully redirected to cart page");

        // Fiyatları karşılaştır
        String cartPrice = cartPage.getItemPrice();
        TestListener.logInfo("Cart price: " + cartPrice);

        Assert.assertTrue(cartPage.verifyPriceMatch(productPrice, cartPrice),
                "Product price doesn't match cart price");
        TestListener.logPass("Product and cart prices match");

        // Miktarı ve toplam fiyat
        TestListener.logInfo("Getting unit price");
        String unitPrice = cartPage.getItemPrice();

        TestListener.logInfo("Increasing quantity to 2");
        cartPage.increaseQuantityTo(2);

        // 2 ürün
        Assert.assertEquals(cartPage.getCartItemsCount(), 2,
                "Cart should contain 2 item type");
        TestListener.logPass("Cart contains 2 item type");

        // Toplam fiyatı
        Assert.assertTrue(cartPage.verifyTotalPriceForQuantity(2, unitPrice),
                "Total price calculation is incorrect for 2 items");
        TestListener.logPass("Total price calculation is correct for 2 items");

        TestListener.logPass("=== Test completed successfully ===");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            TestListener.logInfo("Test completed - Closing driver");
            driver.quit();
        }
    }
}