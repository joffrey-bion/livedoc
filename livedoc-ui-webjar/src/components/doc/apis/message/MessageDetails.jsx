// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { AsyncMessageDoc } from '../../../../model/livedoc';
import { StageBadge } from '../../../shared/content/StageBadge';
import { TypeRef } from '../../typeref/TypeRef';
import { AuthInfo } from '../AuthInfo';
import { HeadersTable } from '../headers/HeadersTable';
import { ParamsTable } from '../params/ParamsTable';

export type MessageDetailsProps = {
  messageDoc: AsyncMessageDoc,
}

export const MessageDetails = ({messageDoc}: MessageDetailsProps) => {
  const doc: AsyncMessageDoc = messageDoc;

  let rows = [];
  if (doc.destinationVariables && doc.destinationVariables.length > 0) {
    rows.push(row('Destination variables', <ParamsTable params={doc.destinationVariables}/>));
  }
  if (doc.headers && doc.headers.length > 0) {
    rows.push(row('Headers', <HeadersTable headers={doc.headers}/>));
  }
  if (doc.payloadType && doc.payloadType.oneLineText !== 'void') {
    rows.push(row('Payload type', <TypeRef type={doc.payloadType}/>));
  }
  if (doc.auth) {
    rows.push(row('Authentication', <AuthInfo auth={doc.auth}/>));
  }
  if (doc.stage) {
    rows.push(row('Stage', <StageBadge stage={doc.stage}/>));
  }

  if (rows.length === 0) {
    if (doc.description) {
      return null;
    } else {
      return <small className="text-muted">No additional information available</small>;
    }
  }

  return <Table>
    <tbody>
    {rows}
    </tbody>
  </Table>;
};

function row(header: string, data) {
  return <tr key={header}>
    <th scope="row">{header}</th>
    <td>{data}</td>
  </tr>;
}
