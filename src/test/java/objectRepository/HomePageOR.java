package objectRepository;

import org.openqa.selenium.By;

public class HomePageOR {
	
	
	public By humbergerMenuDashBoard = By.xpath("//strong[text()='Dashboard']");
	public By operationsMenu= By.xpath("//span[text()='Operations']/ancestor::mat-list-item");
	public By opportunityTrackerSubMenu= By.xpath("//span[contains(@class,'app-menu__label') and text()='Opportunity Tracker']");
	public By oppoTrackHeader= By.xpath("//strong[text()='Opportunity Tracker']");
	public By searchField= By.xpath("//input[contains(@placeholder,'Search')]");
	
	public By dashBoardSideMenu =By.xpath("//span[text()='Dashboard']");
	
	public By pendingCommentReasonAddedCount= By.xpath("(//div[@class='card-bottom multipal-category pending-actions-block']//div)[4]");

}
