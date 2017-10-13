// @flow
import * as React from 'react';
import type { ApiFlowDoc, LivedocID } from '../../../../model/livedoc';
import { ContentHeader } from '../ContentHeader';
import { ApiMethodPanel } from '../api/ApiMethodPanel';

export type ApiFlowPanelProps = {
  flowDoc: ApiFlowDoc,
  onMethodSelect: (id: string) => void,
  onTypeClick: (id: LivedocID) => void,
}

export const FlowDocPanel = ({flowDoc, onMethodSelect, onTypeClick}: ApiFlowPanelProps) => {
  const flow: ApiFlowDoc = flowDoc;

  const preconditions = flow.preconditions.map(cond => <li>{cond}</li>);
  const methodPanels = flow.methods.map(m => <ApiMethodPanel key={m.id} methodDoc={m} onTypeClick={onTypeClick}/>);

  return <section>
    <ContentHeader title={flow.name} description={flow.description}/>
    <h2>Preconditions</h2>
    <ul>
      {preconditions}
    </ul>
    {methodPanels}
  </section>;
};

