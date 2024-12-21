package com.lfernandes.bot.triagem.selenium;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ServiceManagerBase {

    public enum Selector {
        Id("id"),
        Class("class"),
        Name("name");

        public final String value;

        private Selector(String value) {
            this.value = value;
        }
    }

    private WebDriver driver;

    public ServiceManagerBase() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", getDownloadDirectory());
        prefs.put("download.prompt_for_download", false);

        options.setExperimentalOption("prefs", prefs);
        this.driver = new ChromeDriver(options);
    }

    protected String getDownloadDirectory() {
        return "C:\\Users\\layls\\workspace\\pessoal\\BotTriagem\\downloads";
    }

    public void Authentication() {

        try {
            String baseUrl = "app.mapfre.com/smbbex/index.do";
            

            String username = "LLFERNAN";
            String password = "Lfernan@123";

            String authenticatedUrl = "https://" + username + ":" + password + "@" + baseUrl;

            this.driver.get(authenticatedUrl);
            this.waitForPageToLoad(60);
            this.driver.get("https://" + baseUrl);
            this.waitForPageToLoad(60);
            // driver.get(baseUrl);

            // System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
            this.driver.quit();
        }
    }

    //<div class="ext-el-mask-msg x-mask-loading" id="ext-gen-top523" style="left: 414px; top: 312px; visibility: visible;"><div>Carregando...</div></div>
    protected void waitForPageToLoad(int timeOut) {
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        for (int i = 0; i < timeOut; i++) {
            try {
                waitSeconds(1);
                String readyState = js.executeScript("return document.readyState").toString();
                    if ("complete".equals(readyState) && !isLoading()) {
                        System.out.println("Página carregada com sucesso!");
                        return;
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("A página não carregou completamente dentro do tempo esperado.");
    }

    private boolean isLoading(){
        try {
            WebElement element = this.driver.findElement(By.cssSelector("[class='ext-el-mask-msg x-mask-loading']"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitSeconds(int seconds) {
        waitMillis(seconds * 1000);
    }

    protected void waitMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    protected void clickButtonById(String elementId) throws Exception {
        clickButton(Selector.Id, elementId);
        /*
         * try {
         * // WebElement element = this.driver.findElement(By.cssSelector("[id='" +
         * // elementId + "']"));
         * WebElement element = this.driver.findElement(By.cssSelector("[id='" +
         * elementId + "']"));
         * element.click();
         * System.out.println("Botão '" + elementId + "' clicado com sucesso!");
         * } catch (Exception e) {
         * System.err.println("Erro ao tentar clicar no botão: " + e.getMessage());
         * throw e;
         * }
         */
    }

    protected void clickButtonByClass(String elementClass) throws Exception {
        clickButton(Selector.Class, elementClass);
        /*
         * try {
         * // WebElement element = this.driver.findElement(By.cssSelector("[id='" +
         * // elementId + "']"));
         * WebElement element = this.driver.findElement(By.cssSelector("[class='" +
         * elementClass + "']"));
         * element.click();
         * System.out.println("Botão '" + elementClass + "' clicado com sucesso!");
         * } catch (Exception e) {
         * System.err.println("Erro ao tentar clicar no botão: " + e.getMessage());
         * throw e;
         * }
         */
    }

    protected void clickButton(Selector findBy, String keySearch) throws Exception {
        clickButton(findBy.value, keySearch);
    }

    protected void clickButton(String findBy, String keySearch) throws Exception {
        try {
            WebElement element = this.driver.findElement(By.cssSelector("[" + findBy + "='" + keySearch + "']"));
            element.click();
            System.out.println("Botão  " + findBy + ":'" + keySearch + "' clicado com sucesso!");
        } catch (Exception e) {
            String messageError = getExceptionSeleniumShortMessage(e.getMessage());
            System.err.println("Erro ao tentar clicar no botão: " + messageError);
            throw new Exception(messageError);
        }
    }

    protected void clickButtonXPath(String tag, String text) throws Exception{
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//"+tag+"[contains(text(),'"+text+"')]")));
            //WebElement element = this.driver.findElement(By.xpath("//"+tag+"[contains(text(),'"+text+"')]"));
            element.click();
            System.out.println("Botão  " + tag + ":'" + text + "' clicado com sucesso!");
        } catch (Exception e) {
            String messageError = getExceptionSeleniumShortMessage(e.getMessage());
            System.err.println("Erro ao tentar clicar no botão: " + messageError);
            throw new Exception(messageError);
        }
    }

    protected void clickButtonByIdInFrame(String elementId, String frameSrc) throws Exception {
        try {
            driver.switchTo().frame(this.driver.findElement(By.cssSelector("[src='" + frameSrc + "']")));
            clickButtonById(elementId);
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            String messageError = getExceptionSeleniumShortMessage(e.getMessage());
            System.err.println("Erro ao tentar clicar no botão no iframe: " + messageError);
            throw new Exception(messageError);
        }
    }

    protected void switchDriveToFrame(String findBy, String keySearch) throws Exception {
        try {
            WebElement frame = this.driver.findElement(By.cssSelector("[" + findBy + "='" + keySearch + "']"));
            driver.switchTo().frame(frame);
        } catch (Exception e) {
            String messageError = getExceptionSeleniumShortMessage(e.getMessage());
            System.err.println("Erro ao tentar mudar foco do documento para iframe: " + messageError);
            throw new Exception(messageError);
        }
    }

    protected void switchDriveToDefault() {
        driver.switchTo().defaultContent();
    }

    protected void setValueToInputTextById(String elementId, String value) throws Exception {
        setValueToInputText(Selector.Id, elementId, value);
    }

    protected void setValueToInputTextByName(String elementName, String value) throws Exception {
        setValueToInputText(Selector.Name, elementName, value);
    }

    private void setValueToInputText(Selector findBy, String keySearch, String value) throws Exception {
        try {
            WebElement inputField = this.driver
                    .findElement(By.cssSelector("[" + findBy.value + "='" + keySearch + "']"));
            inputField.clear();
            inputField.sendKeys(value);
        } catch (Exception e) {
            String messageError = getExceptionSeleniumShortMessage(e.getMessage());
            System.err.println("Erro ao tentar inserir valor ao inputText '" + keySearch + "': " + messageError);
            throw new Exception(messageError);
        }
    }

    public boolean renameLatestExportFileTo(String renameFile, int timeOut) {
        File exportFile = new File(getDownloadDirectory(), "export.csv");
        for (int i = 0; i < timeOut; i++) {
            try {
                waitSeconds(1);
                if (exportFile.exists()) {
                    return exportFile.renameTo(new File(getDownloadDirectory(), renameFile));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected String getExceptionSeleniumShortMessage(String fullMessage){
        String message = fullMessage.substring(0 , fullMessage.indexOf("}") + 1);
        return message;
    }

    protected WebDriver getDriver() {
        return this.driver;
    }

}
