package testNG_HR;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;


public class Employee {
	public WebDriver driver; 
	java.util.List<WebElement> row;
	public corefun core_fun = new corefun();
	
	
	@BeforeClass
	@Parameters("browser")
	  public void beforeClass(String browser) {	
		System.out.println("befor class");
		if(browser.equalsIgnoreCase("chrome")) {
		System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");	 
		}else if (browser.equalsIgnoreCase("firefox")) { 
		System.setProperty("webdriver.gecko.driver", "lib\\geckodriver.exe");
		}
	}
	
	@BeforeMethod
	@Parameters("browser")
	 public void beforeMethod(String browser) {	
		System.out.println("befor Method");
		if(browser.equalsIgnoreCase("chrome")) {
		driver = new ChromeDriver() ;
		}else if (browser.equalsIgnoreCase("firefox")) { 
		driver = new FirefoxDriver();
		}
	}
	
  @Test
  @Parameters({ "Employee_Username", "Employee_Password","vacation_startdate","vacation_enddate" , "vacation_type" })
  //add vacation request with type has no attachment 
  public void Add_vacation_Request(String Employee_Username , String Employee_Password ,String vacation_startdate,String vacation_enddate , String vacation_type) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  core_fun.Core_add_vacation_request(driver, vacation_startdate, vacation_enddate, vacation_type);
	//validate adding new vacation request successfully 
	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
	  String expected_massage = "تم حفظ البيانات";
	  Assert.assertEquals(actual_massage, expected_massage);
	  driver.switchTo().parentFrame();
	  driver.findElement(By.id("cboxClose")).click();
	  System.out.println("vacation request with no attachment added successfully");
	  driver.close();
  }
  @Test
  @Parameters({ "Employee_Username", "Employee_Password","vacation_startdate","vacation_enddate" , "vacation_type" })
  //add another vacation request when their is another one under operation
  public void Add_another_vacation_Request(String Employee_Username , String Employee_Password ,String vacation_startdate,String vacation_enddate , String vacation_type) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  core_fun.Core_add_vacation_request(driver, vacation_startdate, vacation_enddate, vacation_type);
	  //validate adding another vacation request when their is another ander approval
	  Assert.assertEquals(driver.findElement(By.className("m-alert__text")).getText(), "لايمكن اضافة اجازة جديدة وهناك اجازات تحت الاجراء");
	  driver.switchTo().parentFrame();
	  driver.findElement(By.id("cboxClose")).click();
	  System.out.println("vacation request can not be added cuz their is another request under operation ");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","vacation_type_edit","vacation_edit_attachment"})
  //change request to one with attachment 
  public void Edit_vacation_Request(String Employee_Username , String Employee_Password ,String vacation_type_edit,String vacation_edit_attachment) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { }
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[2]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  Select oSelect = new Select(driver.findElement(By.id("Leave_LeavesTypeId")));
    	oSelect.selectByVisibleText(vacation_type_edit);
    	//upload attachment
    	 driver.findElement(By.id("Leave_AttechmentFile")).sendKeys(vacation_edit_attachment);
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "تم حفظ البيانات";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	 System.out.println("vacation edit successfully to type with attachment ");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","vacation_edit_startdate"})
  // edit vacation start date to be conflict with another vacation request 
  public void Edit_vacation_Startdate_Request(String Employee_Username , String Employee_Password ,String vacation_edit_startdate) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { }
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[3]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Leave_StartDate")).clear();
	  driver.findElement(By.id("Leave_StartDate")).sendKeys(vacation_edit_startdate);
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "لايمكن طلب ااكثر من اجازة لنفس الفترة";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("vacation edit start date to be conflict with another vacation request");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","vacation_edit_startdate2"})
//edit vacation start date to be after end date 
  public void Edit_vacation_Startdate_Request2(String Employee_Username , String Employee_Password ,String vacation_edit_startdate2) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { }
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[3]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Leave_StartDate")).clear();
	  driver.findElement(By.id("Leave_StartDate")).sendKeys(vacation_edit_startdate2);
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "حقول \"المدخلات غير صالحة\" ، الرجاء التحقق من إدخالك.";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("vacation edit start date to be after end date ");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","vacation_edit_enddate"})
//edit vacation end date to be conflict with week end 
  public void Edit_vacation_enddate_Request(String Employee_Username , String Employee_Password ,String vacation_edit_enddate) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { }
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[3]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Leave_EndDate")).clear();
	  driver.findElement(By.id("Leave_EndDate")).sendKeys(vacation_edit_enddate);
	  Actions actions = new Actions(driver);
	  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "يجب ألا تكون نهاية الاجازة يوم عطلة";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("vacation edit end date to be conflict with week end");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","vacation_edit_enddate2"})
//edit vacation end date to order more than available balance for that type 
  public void Edit_vacation_enddate_Request2(String Employee_Username , String Employee_Password ,String vacation_edit_enddate2) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { }
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[3]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Leave_EndDate")).clear();
	  driver.findElement(By.id("Leave_EndDate")).sendKeys(vacation_edit_enddate2);
	  Actions actions = new Actions(driver);
	  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "الرصيد السنوي لا يكفي";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("vacation edit end date to order more than available balance for that type");
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password"})
  public void Delete_vacation_Request(String Employee_Username, String Employee_Password) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  row = driver.findElements(By.tagName("tr"));
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[8]/span/a[4]")).click();
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  Alert simpleAlert = driver.switchTo().alert();
	  String alertText = simpleAlert.getText();
	  System.out.println("Alert text is " + alertText);
	  simpleAlert.accept();
	  System.out.println("vacation request deleted successfully" );
	  driver.close();
  }
  
  @Test
  @Parameters({ "Employee_Username", "Employee_Password","mandate_startdate","mandate_enddate" , "mandate_type","mandate_place" })
  //add mandate request happy bath 
  public void Add_Mandate_Request(String Employee_Username , String Employee_Password ,String mandate_startdate,String mandate_enddate , String mandate_type,String mandate_place) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  core_fun.Core_add_mandate_request( driver,  mandate_startdate, mandate_enddate ,  mandate_type, mandate_place);
	  //validate adding new vacation request successfully 
	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
	  String expected_massage = "تم حفظ البيانات";
	  Assert.assertEquals(actual_massage, expected_massage);
	  driver.switchTo().parentFrame();
	  driver.findElement(By.id("cboxClose")).click();
	  System.out.println("Mandate added successfully" );
	  driver.close();
  }
  @Test
  @Parameters({ "Employee_Username", "Employee_Password","mandate_startdate","mandate_enddate" , "mandate_type","mandate_place" })
  //add another mandate request when this is another one under approval 
  public void Add_ِِAnother_Mandate_Request(String Employee_Username , String Employee_Password ,String mandate_startdate,String mandate_enddate , String mandate_type,String mandate_place) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  core_fun.Core_add_mandate_request( driver, mandate_startdate, mandate_enddate ,  mandate_type, mandate_place);
	  //validate adding another mandate request while their is another one under approval  
	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
	  String expected_massage = "لايمكن اضافة انتداب جديد وهناك انتدابات تحت الاجراء";
	  Assert.assertEquals(actual_massage, expected_massage);
	  driver.switchTo().parentFrame();
	  driver.findElement(By.id("cboxClose")).click();
	  System.out.println("add another mandate while their is another one under approval" );
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","mandate_add_notice"})
  //edit mandate happy bath 
  public void Edit_Mandate_Request(String Employee_Username , String Employee_Password ,String mandate_add_notice) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { } not used yet 
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[10]/span/a[2]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Mandate_Notes")).sendKeys(mandate_add_notice);
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "تم حفظ البيانات";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("Mandate request edit successfully " );
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","mandate_edit_startdate"})
  //edit mandate request start date to be before contract start date   
  public void Edit_Mandate_startdate(String Employee_Username , String Employee_Password ,String mandate_edit_startdate ) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { } not used yet 
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[10]/span/a[2]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Mandate_StartDate")).clear();
	  driver.findElement(By.id("Mandate_StartDate")).sendKeys(mandate_edit_startdate);
	  //scroll to bottom of the page 
	  JavascriptExecutor js = (JavascriptExecutor) driver;
	  js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "يجب أن تكون بداية الانتداب داخل فترة العقد";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("Mandate request edit start date to be before contract start date" );
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","mandate_edit_startdate1"})
  //edit mandate request start date to be conflict with official vacation  
  public void Edit_Mandate_startdate1(String Employee_Username , String Employee_Password ,String mandate_edit_startdate1 ) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { } not used yet 
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[10]/span/a[2]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Mandate_StartDate")).clear();
	  driver.findElement(By.id("Mandate_StartDate")).sendKeys(mandate_edit_startdate1);
	  //scroll to bottom of the page 
	  JavascriptExecutor js = (JavascriptExecutor) driver;
	  js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	  //click title  of the ifram to close pop up 
	  Actions actions = new Actions(driver);
	  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "يجب ألا تكون بداية الانتداب يوم اجازة رسمية";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("Mandate request edit start date to be conflict with official vacation" );
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password","mandate_edit_enddate"})
  //edit mandate request end date to conflict with another request 
  public void Edit_Mandate_enddate(String Employee_Username , String Employee_Password ,String mandate_edit_enddate ) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  //get last request added row number 
	   row = driver.findElements(By.tagName("tr"));
	  System.out.println(row.size());
	  //for (int i=1 ; i<=row.size() ;i++ ) { } not used yet 
	  //use last row number - head of the table 
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[10]/span/a[2]")).click();
	  driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/iframe")));
	  driver.findElement(By.id("Mandate_EndDate")).clear();
	  driver.findElement(By.id("Mandate_EndDate")).sendKeys(mandate_edit_enddate);
	  //scroll to bottom of the page 
	  /*JavascriptExecutor js = (JavascriptExecutor) driver;
	  js.executeScript("window.scrollBy(0,document.body.scrollHeight)");*/
	//go to click on top popup titel to close pop window that cover rest of the window  
	  Actions actions = new Actions(driver);
	  actions.moveToElement(driver.findElement(By.id("popupTitle"))).click().perform();
    	driver.findElement(By.xpath("//*[@id=\"form-popup\"]/div[3]/div/button")).click();
    	//validate Edit vacation request successfully 
  	  String actual_massage = driver.findElement(By.className("m-alert__text")).getText();
  	  String expected_massage = "لايمكن طلب اكثر من انتداب لنفس الفترة";
  	  Assert.assertEquals(actual_massage, expected_massage);
  	System.out.println("Mandate request edit end date to be conflict with another request" );
	  driver.close();
  }
  @Test
  @Parameters({"Employee_Username", "Employee_Password"})
  //delete mandate happy bath 
  public void Delete_Mandate_Request(String Employee_Username, String Employee_Password) {
	  core_fun.Core_login(driver, Employee_Username, Employee_Password);
	  driver.navigate().to("site needed link");
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  row = driver.findElements(By.tagName("tr"));
	  driver.findElement(By.xpath("//*[@id=\"local_data\"]/table/tbody/tr["+(row.size()-1)+"]/td[10]/span/a[3]")).click();
	  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  Alert simpleAlert = driver.switchTo().alert();
	  String alertText = simpleAlert.getText();
	  System.out.println("Alert text is " + alertText);
	  simpleAlert.accept();
	  System.out.println("Mandate deleted successfully " );
	  driver.close();
  }

  

}
