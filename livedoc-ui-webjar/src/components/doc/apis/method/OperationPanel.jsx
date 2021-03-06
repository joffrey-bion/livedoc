// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { Badge, Card, CardBody, CardHeader, CardText, Collapse } from 'reactstrap';
import type { ApiOperationDoc, ApiVerb } from '../../../../model/livedoc';
import { Html } from '../../../shared/content/Html';
import { ApiOperationDetails } from './ApiOperationDetails';
import './OperationPanel.css';

export type OperationPanelProps = {
  operationDoc: ApiOperationDoc,
  open: boolean,
  collapseUrl: string,
  expandUrl: string,
}

export const OperationPanel = ({operationDoc, open, collapseUrl, expandUrl}: OperationPanelProps) => {

  const title = operationDoc.paths || operationDoc.name;
  const verbs = operationDoc.verbs.map(v => <Badge key={v} style={getStyle(v)}>{v}</Badge>);
  const linkUrl = open ? collapseUrl : expandUrl;

  return <Card style={{marginBottom: '15px'}}>
    <CardHeader className="api-method-header" tag={Link} to={linkUrl}>{title} {verbs}</CardHeader>
    <Collapse isOpen={open}>
      <CardBody>
        {operationDoc.description && <CardText><Html content={operationDoc.description}/></CardText>}
        <ApiOperationDetails operationDoc={operationDoc}/>
      </CardBody>
    </Collapse>
  </Card>;
};

const verbColors: {[ApiVerb]: string} = {
  GET: '#468847',
  POST: '#3A87AD',
  PUT: '#F89406',
  PATCH: '#5bc0de',
  DELETE: '#B94A48',
  HEAD: '#AA9A66',
  OPTIONS: '#6B5463',
  TRACE: '#8E6C6E',
};

function getStyle(verb: ApiVerb) {
  return {backgroundColor: verbColors[verb]};
}
