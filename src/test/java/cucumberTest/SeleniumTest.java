package cucumberTest;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTest {
	
	private static WebDriver driver = null;

	public static void main(String[] args) {
		// Create a new instance of the Firefox driver
		 
        driver = new FirefoxDriver();
 
        //Put a Implicit wait, this means that any search for elements on the page could take the time the implicit wait is set for before throwing exception
 
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
 
        //Launch the Online Store Website
 
        driver.get("http://localhost:8080/sellemwb");
 
        // Find the element that's ID attribute is 'userName'
 
       // driver.findElement(By.xpath(".//*[@id='userName']/a")).click();
 
        // Find the element that's ID attribute is 'log' (Username)
 
        // Enter Username on the element found by above desc.
 
        driver.findElement(By.id("userName")).sendKeys("Braffa");
 
        // Find the element that's ID attribute is 'pwd' (Password)
 
        // Enter Password on the element found by the above desc.
 
        driver.findElement(By.id("password")).sendKeys("kelly1233");
 
        // Now submit the form. WebDriver will find the form for us from the element
 
        driver.findElement(By.id("login")).click();
        
        // Print a Log In message to the screen
 
        System.out.println("Login Successfully");
 
        
        driver.findElement(By.id("signOut")).click();
 

 
        // Print a Log In message to the screen
 
        System.out.println("LogOut Successfully");
 
        // Close the driver
 
        driver.quit();

	}

}
