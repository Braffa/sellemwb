package stepDefinition;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.AssertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;

public class TestSteps {

	private static WebDriver driver = null;

	@Given("^User is on Home Page$")
	public void User_is_on_Home_Page() throws Throwable {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/sellemwb");
	}

	@Given("^User enters \"([^\"]*)\" and \"([^\"]*)\"$")
	public void User_enters_and(String userName, String password) throws Throwable {
		driver.findElement(By.id("userName")).sendKeys(userName);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login")).click();
	}

	@Then("^Full Catalogue Page is rendered$")
	public void Full_Catalogue_Page_is_rendered() throws Throwable {
		String heading = driver.findElement(By.id("heading")).getText();
		Assert.assertEquals(heading, "Full Catalogue");
	}

	@When("^User LogOut from the Application$")
	public void User_LogOut_from_the_Application() throws Throwable {
		driver.findElement(By.id("signOut")).click();
	}

	@Then("^Home Page is rendered$")
	public void Home_Page_is_rendered() throws Throwable {
		String heading = driver.findElement(By.id("heading")).getText();
		Assert.assertEquals(heading, "Home Page");
	}

}
