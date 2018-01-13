// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiFlowDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getFlow } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelpler';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { FlowMethodPanel } from './FlowMethodPanel';

export type ApiFlowPanelProps = {
  flowDoc: ?ApiFlowDoc,
  selectedMethodId: ?LivedocID,
}

const FlowDocPanelPresenter = ({flowDoc, selectedMethodId}: ApiFlowPanelProps) => {
  if (!flowDoc) {
    return <Redirect to={RouteHelper.flowsUrl()}/>;
  }
  const flow: ApiFlowDoc = flowDoc;

  const preconditions = flow.preconditions.map(cond => <li>{cond}</li>);
  const methodPanels = flow.methods.map(m => {
    const open = m.livedocId === selectedMethodId;
    return <FlowMethodPanel key={m.livedocId} methodDoc={m} open={open}/>;
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
  selectedMethodId: ?LivedocID,
}

const mapStateToProps = (state: State, {flowId, selectedMethodId}: FlowDocPanelOwnProps) => ({
  flowDoc: flowId && getFlow(flowId, state),
  selectedMethodId: selectedMethodId,
});

const mapDispatchToProps = {};

export const FlowDocPanel = connect(mapStateToProps, mapDispatchToProps)(FlowDocPanelPresenter);


