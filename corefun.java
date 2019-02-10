package testNG_HR;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class corefun {

	public void HR_login(WebDriver driver, String username, String password) {
		// TODO Auto-generated method stub
		System.out.println("go to fun");
		driver.manage().window().maximize();
		driver.get("http://hr.wesamsoft.com");
		driver.findElement(By.id("Username")).sendKeys(username);
		driver.findElement(By.id("Password")).sendKeys(password);
		driver.findElement(By.id("btn_submit")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}

}
