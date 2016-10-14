

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.spaceheroes.util.FlowUtils;

public class TestRun {
	
	
	@Test
	public void testPackage() throws Exception {
		
		List<String> flowNames = new ArrayList<String>();
		flowNames.add("Update_Contact_when_Campaign_Member_Sales_Owner_is_Updated");
		flowNames.add("Update_Lead_when_Campaign_Member_Sales_Owner_is_Updated");
		
		FlowUtils.createFlowInactivationPack("flow-inactivations", flowNames);
		FlowUtils.createFlowDeletionPack("flow-deletions", flowNames);
		FlowUtils.createFlowDownloadAllPack("existing-flows");
		
		FlowUtils.copyFlowsAndIncreaseVersion("data/src/flows", "data/src/flows-copy");
		
	}
	

}
