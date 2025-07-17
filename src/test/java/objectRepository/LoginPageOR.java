package objectRepository;

import org.openqa.selenium.By;

public class LoginPageOR {
	public By txtUsername= By.name("data[User][email]");
	public By txtPwd= By.name("data[User][password]");
	public By buttLogin = By.xpath("//button[text()='Login' and @type='submit']");
}
