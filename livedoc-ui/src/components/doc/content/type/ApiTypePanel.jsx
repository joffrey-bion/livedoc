// @flow
import * as React from 'react';
import { Panel, Table } from 'react-bootstrap';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { ApiFieldRow } from './ApiFieldRow';

type Props = {
  typeDoc: ApiObjectDoc
}

export const ApiTypePanel = (props: Props) => {
  const doc: ApiObjectDoc = props.typeDoc;

  let fields = doc.fields.map(f => <ApiFieldRow key={f.name} field={f}/>);

  return <Panel header={doc.name}>
    <Table striped>
      {fields}
    </Table>
  </Panel>;
};

