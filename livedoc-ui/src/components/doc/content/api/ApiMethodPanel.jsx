// @flow
import * as React from 'react';
import { Badge, Card, CardBody, CardHeader, CardText, Collapse } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import { ApiMethodDetails } from './ApiMethodDetails';

export type ApiMethodPanelProps = {
  methodDoc: ApiMethodDoc,
}

export class ApiMethodPanel extends React.Component<ApiMethodPanelProps> {

  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }

  toggleOpen() {
    this.setState((prevState) => ({
      isOpen: !prevState.isOpen
    }));
  }

  render() {
    const doc: ApiMethodDoc = this.props.methodDoc;
    const toggleOpen = () => this.toggleOpen();

    const title = doc.path || doc.method;
    const verbs = doc.verb.map(v => <Badge key={v} style={getStyle(v)}>{v}</Badge>);

    return <Card style={{marginBottom: '15px'}}>
      <CardHeader onClick={toggleOpen}>{title} {verbs}</CardHeader>
      <Collapse isOpen={this.state.isOpen}>
        <CardBody>
          <CardText>{doc.description}</CardText>
          <ApiMethodDetails methodDoc={doc}/>
        </CardBody>
      </Collapse>
    </Card>;
  }
}

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
