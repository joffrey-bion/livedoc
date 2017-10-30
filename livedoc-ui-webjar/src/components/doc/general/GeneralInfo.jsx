// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { Livedoc } from '../../../model/livedoc';

export type GeneralInfoProps = {
  livedoc: Livedoc
}

export const GeneralInfo = (props: GeneralInfoProps) => {
  return <aside>
    <h4 style={{marginTop: '0.5rem'}}>API INFO</h4>
    <small>
      <Table className="w-50 table-sm">
        <tbody>
        <tr>
          <th scope="row">Base path</th>
          <td>{props.livedoc.basePath}</td>
        </tr>
        <tr>
          <th scope="row">Version</th>
          <td>{props.livedoc.version}</td>
        </tr>
        </tbody>
      </Table>
    </small>
  </aside>;
};

