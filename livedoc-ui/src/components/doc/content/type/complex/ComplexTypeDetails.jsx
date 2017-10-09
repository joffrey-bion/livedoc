// @flow
import * as React from 'react';
import { Table } from 'react-bootstrap';
import type { ApiObjectDoc } from '../../../../../model/livedoc';
import { ApiFieldRow } from './ApiFieldRow';

type Props = {
  typeDoc: ApiObjectDoc,
  onSelect: (id: string) => void,
}

export const ComplexTypeDetails = (props: Props) => {
  const doc: ApiObjectDoc = props.typeDoc;

  let fieldRows = doc.fields.map(f => <ApiFieldRow key={f.name} field={f} onSelect={props.onSelect}/>);

  return <Table striped>
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
  </Table>;
};
