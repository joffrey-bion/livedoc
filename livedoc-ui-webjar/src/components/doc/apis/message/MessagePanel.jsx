// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { Badge, Card, CardBody, CardHeader, CardText, Collapse } from 'reactstrap';
import type { AsyncCommand, AsyncMessageDoc, LivedocID } from '../../../../model/livedoc';
import { RouteHelper } from '../../../../routing/routeHelper';
import { Html } from '../../../shared/content/Html';
import { MessageDetails } from './MessageDetails';

export type MessagePanelProps = {
  messageDoc: AsyncMessageDoc,
  parentApiId: LivedocID,
  open: boolean,
}

export const MessagePanel = ({messageDoc, parentApiId, open}: MessagePanelProps) => {
  const collapseUrl = RouteHelper.apiUrl(parentApiId);
  const expandUrl = RouteHelper.operationUrl(parentApiId, messageDoc.livedocId);

  const title = messageDoc.destinations || messageDoc.name;
  const command = <Badge style={getStyle(messageDoc.command)}>{messageDoc.command}</Badge>;
  const linkUrl = open ? collapseUrl : expandUrl;

  return <Card style={{marginBottom: '15px'}}>
    <CardHeader className="api-method-header" tag={Link} to={linkUrl}>{command} {title}</CardHeader>
    <Collapse isOpen={open}>
      <CardBody>
        {messageDoc.description && <CardText><Html content={messageDoc.description}/></CardText>}
        <MessageDetails messageDoc={messageDoc}/>
      </CardBody>
    </Collapse>
  </Card>;
};

const commandColors: {[AsyncCommand]: string} = {
  SEND: '#468847',
  SUBSCRIBE: '#3A87AD',
};

function getStyle(command: AsyncCommand) {
  return {backgroundColor: commandColors[command]};
}
