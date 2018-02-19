// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiHeaderDoc } from '../../../../../model/livedoc';
import { HeaderRow } from './HeaderRow';

export type ApiMethodHeadersTableProps = {
  headers: ApiHeaderDoc[],
}

export const ApiMethodHeadersTable = (props: ApiMethodHeadersTableProps) => {
  const headers: ApiHeaderDoc[] = props.headers;

  const showDescription = headers.filter(h => h.description).length > 0;
  const headerRows = headers.map((h, index) => <HeaderRow key={index} header={h}/>);

  return <Table striped size="sm">
    <thead>
    <tr>
      <th>Name</th>
      <th>Value</th>
      {showDescription && <th>Description</th>}
    </tr>
    </thead>
    <tbody>
    {headerRows}
    </tbody>
  </Table>;
};
