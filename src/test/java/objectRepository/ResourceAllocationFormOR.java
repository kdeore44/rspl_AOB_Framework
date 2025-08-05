package objectRepository;

import org.openqa.selenium.By;

public class ResourceAllocationFormOR {
	
	
	public By formTable= By.xpath("//div[@class='horizontal-scroll-format']/table");
	public By tableRows= By.xpath("//div[@class='tag-resources-dialog open']//table//tbody/tr[not(contains(@class,'example-detail-row')) and not(contains(@class,'expanded'))]");
	public By dropDownOption= By.xpath("//mat-option/span[text()='Billable'] | //option[text()='Billable']");
	public By submitBtn= By.xpath("//div[@class='request-resources-footer']//button[normalize-space()='Submit']");
	public By successmsg= By.xpath("//div[contains(text(), 'successfully') or contains(text(),'Success')]");
}
