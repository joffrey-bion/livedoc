// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiObjectDoc, LivedocID } from '../../../../../model/livedoc';
import { ApiFieldRow } from './ApiFieldRow';

export type ComplexTypeDetailsProps = {
  typeDoc: ApiObjectDoc,
  onTypeClick: (id: LivedocID) => void,
}

export const ComplexTypeDetails = (props: ComplexTypeDetailsProps) => {
  const doc: ApiObjectDoc = props.typeDoc;

  let fieldRows = doc.fields.map(f => <ApiFieldRow key={f.name} field={f} onTypeClick={props.onTypeClick}/>);

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
