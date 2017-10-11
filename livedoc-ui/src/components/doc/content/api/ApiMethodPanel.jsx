// @flow
import * as React from 'react';
import { Badge, Panel } from 'react-bootstrap';
import type { ApiMethodDoc, LivedocID } from '../../../../model/livedoc';
import { ApiMethodDetails } from './ApiMethodDetails';

export type ApiMethodPanelProps = {
  methodDoc: ApiMethodDoc,
  onTypeClick: (id: LivedocID) => void,
}

export const ApiMethodPanel = (props: ApiMethodPanelProps) => {
  const doc: ApiMethodDoc = props.methodDoc;

  const title = doc.path || doc.method;
  const verbs = doc.verb.map(v => <Badge pullRight style={getStyle(v)}>{v}</Badge>);
  const header = <h4>{title} {verbs}</h4>;
  return <Panel header={header} headerRole='button' collapsible>
    <p>{doc.description}</p>
    <ApiMethodDetails methodDoc={doc} onTypeClick={props.onTypeClick}/>
  </Panel>;
};

const verbColors = {
  GET: '#468847',
  POST: '#3A87AD',
  PUT: '#F89406',
  PATCH: '#5bc0de',
  DELETE: '#B94A48',
  HEAD: '#AA9A66',
  OPTIONS: '#6B5463',
  TRACE: '#8E6C6E',
};

function getStyle(verb: string) {
  return {backgroundColor: verbColors[verb]};
}
