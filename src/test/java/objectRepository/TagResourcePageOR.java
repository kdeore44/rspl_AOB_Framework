package objectRepository;

import org.openqa.selenium.By;

import automationEngine.ApplicationSetup;

public class TagResourcePageOR extends ApplicationSetup {
	
	public By tagtableRows= By.xpath("(//table)[2]//tbody/tr[not(contains(@class,'example-detail-row'))]");
	public By srcTaggedicon= By.xpath("//table//tbody/tr/td/div/button/span");
	public By tagSearchField= By.xpath("//div[@role='menu']//following::input[@placeholder='Search']");
	public By hireNewResourcebtn= By.xpath("//button[text()='Hire New Resource']");
	public By tagresourcehoursfield= By.xpath("//input[@type='number']");
	public By confirmbtn= By.xpath("//button[normalize-space()='Confirm']");
	public By addReasonBtn= By.xpath(".//td//mat-icon[text()='notes']");
	public By reasonSectionTitle= By.xpath("//h1[contains(text(),'Reason Section For')]");
	public By leaveaReasonLabel= By.xpath("//mat-label[contains(text(),'Leave a reason')]");
	public By selectReasonDropdown=By.xpath("//mat-select[@placeholder='Select Reason']");
	public By addReasonbtnOnReasonSection= By.xpath("//span[normalize-space(text())='Add Reason']");
	public By SuccessMsgAddReson= By.xpath("//div[normalize-space(text())='Reason Added Successfully']");
	public By closeIconSuccessReasonPopUp= By.xpath("//button[@mattooltip='Close']");
	
	//Verify add reason from tagged resource TEST
	public By rowsAddReason= By.xpath("(//tbody[(@role='rowgroup')])[4]//tr[1]");
	                            
	
	public By addReasonIcon= By.xpath("//div[@class='tag-resources-dialog ng-tns-c2562928263-283 open']//tbody//tr[not(contains(@class,'example-detail-row'))]//td//mat-icon[text()='notes']");
	
}
