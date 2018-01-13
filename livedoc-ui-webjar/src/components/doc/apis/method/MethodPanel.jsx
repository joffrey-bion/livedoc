// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { Badge, Card, CardBody, CardHeader, CardText, Collapse } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import { ApiMethodDetails } from './ApiMethodDetails';
import './MethodPanel.css';

export type MethodPanelProps = {
  methodDoc: ApiMethodDoc,
  open: boolean,
  collapseUrl: string,
  expandUrl: string,
}

export const MethodPanel = ({methodDoc, open, collapseUrl, expandUrl}: MethodPanelProps) => {

  const title = methodDoc.path || methodDoc.method;
  const verbs = methodDoc.verb.map(v => <Badge key={v} style={getStyle(v)}>{v}</Badge>);
  const linkUrl = open ? collapseUrl : expandUrl;

  return <Card style={{marginBottom: '15px'}}>
    <CardHeader className="api-method-header" tag={Link} to={linkUrl}>{title} {verbs}</CardHeader>
    <Collapse isOpen={open}>
      <CardBody>
        <CardText>{methodDoc.description}</CardText>
        <ApiMethodDetails methodDoc={methodDoc}/>
      </CardBody>
    </Collapse>
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
