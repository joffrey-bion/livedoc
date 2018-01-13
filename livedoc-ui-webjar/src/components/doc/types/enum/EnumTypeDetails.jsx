// @flow
import * as React from 'react';
import { Card, Table } from 'reactstrap';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { EnumValueRow } from './EnumValueRow';

export type EnumTypeDetailsProps = {
  typeDoc: ApiObjectDoc
}

export const EnumTypeDetails = ({typeDoc}: EnumTypeDetailsProps) => {
  const values = typeDoc.allowedvalues || [];
  const valueRows = values.map(v => <EnumValueRow key={v} value={v} description=""/>);

  return <Card>
    <Table striped>
      <thead>
      <tr>
        <th>Value</th>
        <th>Description</th>
      </tr>
      </thead>
      <tbody>
      {valueRows}
      </tbody>
    </Table>
  </Card>;
};
