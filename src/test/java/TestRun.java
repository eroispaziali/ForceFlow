

import org.junit.Test;

import com.spaceheroes.util.FlowUtils;

public class TestRun {
	
	@Test
	public void testCreateFlowDownloadAll() throws Exception {
		FlowUtils.createFlowDownloadAllPack("ff-output/1-flows-download");
		FlowUtils.createFlowInactivation("data/src/flows", "ff-output/2-flows-inactivation");
		// TODO: deploy deactivations with migration tool
		FlowUtils.createFlowDeletionPack("ff-output/2-flows-inactivation", "ff-output/3-flows-delete");
		// TODO: deploy with migration tool
	}
	
}
