package objectRepository;

import org.openqa.selenium.By;

public class LoginPageORAOB {
	
	public By btnSigninMicrosoft= By.xpath("//span[@class='mat-mdc-button-touch-target']");
	public By txtUsername= By.xpath("//input[@type='email']");
			
	public By nextbtn= By.xpath("//input[@type='submit']");
	public By txtPassword= By.xpath("//input[@type='password']");
	public By signInbtn= By.xpath("//input[@type='submit']");

	public By checkBox=By.xpath("//input[@type='checkbox']");
	public By yesbtn= By.id("idSIButton9");

}
