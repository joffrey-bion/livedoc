// @flow
import * as React from 'react';
import { Accordion, Panel } from 'react-bootstrap';
import type { ApiFlowDoc } from '../../../../model/livedoc';
import { ContentHeader } from '../ContentHeader';
import { ApiMethodPanel } from '../api/ApiMethodPanel';

export type ApiFlowPanelProps = {
  flowDoc: ApiFlowDoc,
  onMethodSelect: (id: string) => void,
  onTypeClick: (id: string) => void,
}

export const FlowDocPanel = ({flowDoc, onMethodSelect, onTypeClick}: ApiFlowPanelProps) => {
  const flow: ApiFlowDoc = flowDoc;

  const preconditions = flow.preconditions.map(p => <li>p</li>);
  const methodPanels = flow.methods.map(m => <ApiMethodPanel key={m.id} methodDoc={m} onTypeClick={onTypeClick}/>);

  return <section>
    <ContentHeader title={flow.name} description={flow.description}/>
    <h2>Preconditions</h2>
    <ul>
      {preconditions}
    </ul>
    <Accordion spaced onSelect={onMethodSelect}>
      {methodPanels}
    </Accordion>
  </section>;
};

