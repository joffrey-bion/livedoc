// @flow
import * as React from 'react';
import { Card, Table } from 'reactstrap';
import type { ApiTypeDoc } from '../../../../model/livedoc';
import { TypeFieldRow } from './TypeFieldRow';

export type ComplexTypeDetailsProps = {
  typeDoc: ApiTypeDoc,
}

export const ComplexTypeDetails = (props: ComplexTypeDetailsProps) => {
  const doc: ApiTypeDoc = props.typeDoc;

  let fieldRows = doc.fields.map(f => <TypeFieldRow key={f.name} field={f}/>);

  return <Card>
    <Table striped>
      <thead>
      <tr>
        <th>Field</th>
        <th>Type</th>
        <th>Description</th>
      </tr>
      </thead>
      <tbody>
      {fieldRows}
      </tbody>
    </Table>
  </Card>;
};
