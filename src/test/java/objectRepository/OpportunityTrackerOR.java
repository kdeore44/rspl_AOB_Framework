package objectRepository;

import org.openqa.selenium.By;

public class OpportunityTrackerOR {

	public By oppotunitySearchField= By.xpath("//input[contains(@placeholder,'Search')]");
	public By allOppoTitle= By.xpath("//h2[normalize-space(text())='All Opportunities']");
	public By allOppoTable= By.xpath("//div[@class='table-wrapper mt-1']");
	
	
	//tag resource
	public By tagResourcebtn= By.xpath("//td[contains(text(),'OPP000001425')]//ancestor::tr//span[text()='Tag Resources']");

}
