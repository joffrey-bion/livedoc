// @flow
import * as React from 'react';
import { Panel, Table } from 'react-bootstrap';
import type { ApiDoc } from '../../../../model/livedoc';
import { ApiMethodPanel } from './ApiMethodPanel';

type Props = {
  apiDoc: ApiDoc
}

export const ApiDocPanel = (props: Props) => {
  const api: ApiDoc = props.apiDoc;

  let methods = api.methods.map(m => <ApiMethodPanel key={m.id} method={m}/>);

  return <Panel header={api.name}>
    <Table striped>
      {methods}
    </Table>
  </Panel>;
};

