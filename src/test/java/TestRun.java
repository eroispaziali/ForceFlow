

import java.util.List;

import org.junit.Test;

import com.spaceheroes.util.FlowUtils;

public class TestRun {
	
	
	@Test
	public void testPackage() throws Exception {
		
		/* stage 1 */
		FlowUtils.createFlowDownloadAllPack("1-flows-download");
		// download flows with migration tool
		
		/* stage 2 */
		String downloadPath = "data/src/flows";
		String tempPath = "2-flows-deactivate";

		List<String> flowNames = FlowUtils.getFlowsNames(downloadPath);
		FlowUtils.copyFlowsAndIncreaseVersion(downloadPath, tempPath + "/flows");
		FlowUtils.createFlowInactivationPack(tempPath, flowNames);
		
		/* stage 3 */
		
		
		
		// deploy deactivations with migration tool
		
		
		
		FlowUtils.createFlowDeletionPack("3-flow-deletions", flowNames);
		
		FlowUtils.copyFlowsAndIncreaseVersion("data/src/flows", "data/src/flows-copy");
		
	}
	

}
