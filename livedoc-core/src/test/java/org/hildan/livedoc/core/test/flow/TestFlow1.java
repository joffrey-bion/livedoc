package org.hildan.livedoc.core.test.flow;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;

@ApiFlowSet
public class TestFlow1 {

    @ApiFlow(name = "Flow1", description = "Description for flow 1",
            steps = {@ApiFlowStep(apiOperationId = "M1"), @ApiFlowStep(apiOperationId = "M2"),
                    @ApiFlowStep(apiOperationId = "M3")})
    public void flow1() {

    }

    @ApiFlow(name = "Flow2", description = "Description for flow 2",
            steps = {@ApiFlowStep(apiOperationId = "M0"), @ApiFlowStep(apiOperationId = "M4"),
                    @ApiFlowStep(apiOperationId = "M5")})
    public void flow2() {

    }

}
