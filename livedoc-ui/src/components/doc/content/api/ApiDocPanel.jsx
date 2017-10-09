// @flow
import * as React from 'react';
import { Accordion, PageHeader } from 'react-bootstrap';
import type { ApiDoc } from '../../../../model/livedoc';
import { ApiMethodPanel } from './ApiMethodPanel';

export type ApiDocPanelProps = {
  apiDoc: ApiDoc,
  onMethodSelect: (id: string) => void,
  onTypeClick: (id: string) => void,
}

export const ApiDocPanel = (props: ApiDocPanelProps) => {
  const api: ApiDoc = props.apiDoc;

  const methodPanels = api.methods.map(m => <ApiMethodPanel key={m.id} methodDoc={m} onTypeClick={props.onTypeClick}/>);

  return <section>
    <PageHeader>{api.name}</PageHeader>
    <p>{api.description}</p>
    <Accordion spaced onSelect={props.onMethodSelect}>
      {methodPanels}
    </Accordion>
  </section>;
};

