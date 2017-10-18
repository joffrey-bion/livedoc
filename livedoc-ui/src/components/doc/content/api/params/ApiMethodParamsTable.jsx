// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiParamDoc } from '../../../../../model/livedoc';
import { ParamRow } from './ParamRow';

export type ApiMethodParamsTableProps = {
  params: ApiParamDoc[],
}

export const ApiMethodParamsTable = (props: ApiMethodParamsTableProps) => {
  const params: ApiParamDoc[] = props.params;

  const paramRows = params.map(p => <ParamRow param={p}/>);

  return <Table striped size="sm">
    <thead>
    <tr>
      <th>Name</th>
      <th>Type</th>
      <th>Description</th>
    </tr>
    </thead>
    <tbody>
    {paramRows}
    </tbody>
  </Table>;
};
