// @flow
import * as React from 'react';
import { Table } from 'react-bootstrap';
import type { ApiParamDoc, LivedocID } from '../../../../../model/livedoc';
import { ParamRow } from './ParamRow';

export type ApiMethodParamsTableProps = {
  params: ApiParamDoc[],
  onTypeClick: (id: LivedocID) => void,
}

export const ApiMethodParamsTable = (props: ApiMethodParamsTableProps) => {
  const params: ApiParamDoc[] = props.params;

  const paramRows = params.map(p => <ParamRow param={p} onTypeClick={props.onTypeClick}/>);

  return <Table striped>
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
