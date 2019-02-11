package testNG_HR;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class corefun {

	public void Core_login(WebDriver driver, String username, String password) {
		// TODO Auto-generated method stub
		System.out.println("go to fun login");
		driver.manage().window().maximize();
		driver.get("site needed link");
		driver.findElement(By.id("Username")).sendKeys(username);
		driver.findElement(By.id("Password")).sendKeys(password);
		driver.findElement(By.id("btn_submit")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}
	public void Core_add_vacation_request(WebDriver driver, String vacation_startdate,String vacation_enddate , String vacation_type) {
		// TODO Auto-generated method stub
		System.out.println("go to fun-add vacation request");
		driver.navigate().to("site needed link");
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  driver.findElement(By.linkText("اضافة اجازة")).click();
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  // go to ifram (add vacation pop up )
		  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
		  //validate add vacation form 
		  driver.findElement(By.id("Leave_StartDate")).clear();
		  driver.findElement(By.id("Leave_EndDate")).clear();
		  Actions actions = new Actions(driver);
		  //close pop up menus 
		  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
		  //validate mandatory fields 
		  driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
		  Assert.assertEquals(driver.findElement(By.id("Leave_StartDate-error")).getText(), "الحقل مطلوب");
		  Assert.assertEquals(driver.findElement(By.id("Leave_EndDate-error")).getText(), "الحقل مطلوب");
		  Assert.assertEquals(driver.findElement(By.id("Leave_LeavesTypeId-error")).getText(), "الحقل مطلوب");
		  // send vacation parameters 
		  //driver.findElement(By.id("Leave_StartDate")).clear();
		  driver.findElement(By.id("Leave_StartDate")).sendKeys(vacation_startdate);
		  //driver.findElement(By.id("Leave_EndDate")).clear();
		  driver.findElement(By.id("Leave_EndDate")).sendKeys(vacation_enddate);
		//go to click on top popup titel to close pop window that cover rest of the window  
		  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
			Select oSelect = new Select(driver.findElement(By.id("Leave_LeavesTypeId")));
			oSelect.selectByVisibleText(vacation_type);
			//submit button 
		  driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
	}
	public void Core_add_mandate_request(WebDriver driver, String mandate_startdate,String mandate_enddate , String mandate_type,String mandate_place) {
		  driver.navigate().to("site needed link");
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  driver.findElement(By.linkText("اضافة طلب انتداب")).click();
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  // go to ifram (add vacation pop up )
		  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
		  // clear form 
		  //driver.findElement(By.linkText("إلغاء")).click();
		  // send vacation parameters 
		  driver.findElement(By.id("Mandate_StartDate")).clear();
		  driver.findElement(By.id("Mandate_StartDate")).sendKeys(mandate_startdate);
		  driver.findElement(By.id("Mandate_EndDate")).clear();
		  driver.findElement(By.id("Mandate_EndDate")).sendKeys(mandate_enddate);
		//go to click on top popup titel to close pop window that cover rest of the window  
		  Actions actions = new Actions(driver);
		  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
			Select oSelect = new Select(driver.findElement(By.id("Mandate_TypeId")));
			oSelect.selectByVisibleText(mandate_type);
			Select oSelect2 = new Select(driver.findElement(By.id("CountryList")));
			oSelect2.selectByVisibleText(mandate_place);
			//submit button 
		  driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button/span/i")).click();
		
		
	}
	

}
