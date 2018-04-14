// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiFlowDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getFlow } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelper';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { FlowOperationPanel } from './FlowOperationPanel';

export type ApiFlowPanelProps = {
  flowDoc: ?ApiFlowDoc,
  selectedOperationId: ?LivedocID,
}

const FlowDocPanelPresenter = ({flowDoc, selectedOperationId}: ApiFlowPanelProps) => {
  if (!flowDoc) {
    return <Redirect to={RouteHelper.flowsUrl()}/>;
  }
  const flow: ApiFlowDoc = flowDoc;

  const preconditions = flow.preconditions.map(cond => <li>{cond}</li>);
  const methodPanels = flow.operations.map(m => {
    const open = m.livedocId === selectedOperationId;
    return <FlowOperationPanel key={m.livedocId} operationDoc={m} open={open}/>;
  });

  return <section>
    <ContentHeader title={flow.name} description={flow.description}/>
    <h2>Preconditions</h2>
    <ul>
      {preconditions}
    </ul>
    {methodPanels}
  </section>;
};

export type FlowDocPanelOwnProps = {
  flowId: LivedocID,
  selectedOperationId: ?LivedocID,
}

const mapStateToProps = (state: State, {flowId, selectedOperationId}: FlowDocPanelOwnProps) => ({
  flowDoc: flowId && getFlow(flowId, state),
  selectedOperationId: selectedOperationId,
});

const mapDispatchToProps = {};

export const FlowDocPanel = connect(mapStateToProps, mapDispatchToProps)(FlowDocPanelPresenter);


