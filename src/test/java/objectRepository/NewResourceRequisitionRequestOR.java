package objectRepository;

import org.openqa.selenium.By;

import automationEngine.ApplicationSetup;

public class NewResourceRequisitionRequestOR extends ApplicationSetup{
	public By hireNewResourceBtn= By.xpath("//button[text()='Hire New Resource']");
	public By resourceFormTitle= By.xpath("//strong[contains(text(),'New Resource Requisition Request')]");
	
	public By yearOfExperience= By.xpath("//span[normalize-space(text())='Experience']");
	public By NoOfPositionfield= By.id("numOfResource");
    public By resourceTypefield= By.xpath("//span[normalize-space(text())='Resource Type']");
    public By requiredDate= By.xpath("(//button[@type='button' and @aria-label='Open calendar'])[2]");
    public By departmentField= By.xpath("//span[normalize-space(text())='Division/Department']");
    public By technologyStack= By.xpath("//span[normalize-space(text())='Technology Stack']");
    public By submitBtn= By.xpath("//button[normalize-space()='Submit Request']");
  
}
