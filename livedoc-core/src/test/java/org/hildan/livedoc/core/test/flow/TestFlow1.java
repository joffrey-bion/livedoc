package org.hildan.livedoc.core.test.flow;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;

@ApiFlowSet
public class TestFlow1 {

    @ApiFlow(name = "Flow1", description = "Description for flow 1",
            steps = {@ApiFlowStep(apiMethodId = "M1"), @ApiFlowStep(apiMethodId = "M2"),
                    @ApiFlowStep(apiMethodId = "M3")})
    public void flow1() {

    }

    @ApiFlow(name = "Flow2", description = "Description for flow 2",
            steps = {@ApiFlowStep(apiMethodId = "M0"), @ApiFlowStep(apiMethodId = "M4"),
                    @ApiFlowStep(apiMethodId = "M5")})
    public void flow2() {

    }

}
