// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiFlowDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getFlow } from '../../../../redux/livedoc';
import { ApiMethodPanel } from '../api/ApiMethodPanel';
import { ContentHeader } from '../ContentHeader';

export type ApiFlowPanelProps = {
  flowDoc: ?ApiFlowDoc,
}

const FlowDocPanel = ({flowDoc}: ApiFlowPanelProps) => {
  if (!flowDoc) {
    return <Redirect to="/flows"/>;
  }
  const flow: ApiFlowDoc = flowDoc;

  const preconditions = flow.preconditions.map(cond => <li>{cond}</li>);
  const methodPanels = flow.methods.map(m => <ApiMethodPanel key={m.id} methodDoc={m}/>);

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
  match: any,
}

const mapStateToProps = (state: State, {match}: FlowDocPanelOwnProps) => ({
  flowDoc: getFlow(match.params.flowId, state),
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(FlowDocPanel);


