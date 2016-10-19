import java.io.IOException;

import com.spaceheroes.util.FlowUtils;

public class Main {

	public static void main(String[] args) throws IOException {
		FlowUtils.createFlowInactivation("data/tmp", "ff-output/2-flows-inactivation", true);
		FlowUtils.createFlowDeletionPack("data/tmp", "ff-output/3-flows-deletion");
	}

}
