package org.example.shelf.documentation.flows;

import org.example.shelf.documentation.DocumentationConstants;
import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;

@ApiFlowSet
public class ShelfFlows {

    @ApiFlow(name = "Author detail flow",
            description = "Gets an author's details starting from the book's list",
            steps = {
                    @ApiFlowStep(apiMethodId = DocumentationConstants.BOOK_FIND_ALL),
                    @ApiFlowStep(apiMethodId = DocumentationConstants.BOOK_FIND_ONE),
                    @ApiFlowStep(apiMethodId = DocumentationConstants.AUTHOR_FIND_ONE)
            })
    public void authorDetailFlow() {

    }

}
