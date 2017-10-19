// @flow
import * as React from 'react';
import { Badge, Card, CardBlock, CardHeader, CardText } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import { ApiMethodDetails } from './ApiMethodDetails';

export type ApiMethodPanelProps = {
  methodDoc: ApiMethodDoc,
}

export const ApiMethodPanel = (props: ApiMethodPanelProps) => {
  const doc: ApiMethodDoc = props.methodDoc;

  const title = doc.path || doc.method;
  const verbs = doc.verb.map(v => <Badge style={getStyle(v)}>{v}</Badge>);

  return <Card style={{marginBottom: '15px'}}>
    <CardHeader role="button">
      <h4>{title} {verbs}</h4>
    </CardHeader>
    <CardBlock>
      <CardText>{doc.description}</CardText>
      <ApiMethodDetails methodDoc={doc}/>
    </CardBlock>
  </Card>;
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
