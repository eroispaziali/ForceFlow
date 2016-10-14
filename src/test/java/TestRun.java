

import java.util.List;

import org.junit.Test;

import com.spaceheroes.util.FlowUtils;

public class TestRun {
	
	
	@Test
	public void testPackage() throws Exception {
		
		FlowUtils.createFlowDownloadAllPack("1-flows-download");
		
		// download flows with metadata API
		
		List<String> flowNames = FlowUtils.getFlowsNames("data/src/flows");
		
		FlowUtils.createFlowInactivationPack("2-flows-deactivate", flowNames);
		FlowUtils.createFlowDeletionPack("3-flow-deletions", flowNames);
		
		FlowUtils.copyFlowsAndIncreaseVersion("data/src/flows", "data/src/flows-copy");
		
	}
	

}
