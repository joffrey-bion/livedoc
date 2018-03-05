// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiOperationDoc } from '../../../../model/livedoc';
import { StageBadge } from '../../../shared/content/StageBadge';
import { AuthInfo } from './AuthInfo';
import { HeadersTable } from './headers/HeadersTable';
import { ParamsTable } from './params/ParamsTable';
import { TypeRefWithMime } from './TypeRefWithMime';

export type ApiOperationDetailsProps = {
  operationDoc: ApiOperationDoc,
}

export const ApiOperationDetails = (props: ApiOperationDetailsProps) => {
  const doc: ApiOperationDoc = props.operationDoc;

  let rows = [];
  if (doc.pathParameters && doc.pathParameters.length > 0) {
    rows.push(row('Path Params', <ParamsTable params={doc.pathParameters}/>));
  }
  if (doc.queryParameters && doc.queryParameters.length > 0) {
    rows.push(row('Query Params', <ParamsTable params={doc.queryParameters}/>));
  }
  if (doc.headers && doc.headers.length > 0) {
    rows.push(row('Headers', <HeadersTable headers={doc.headers}/>));
  }
  if (doc.requestBody) {
    rows.push(row('Request body type', <TypeRefWithMime type={doc.requestBody.type} mimeTypes={doc.consumes}/>));
  }
  if (doc.responseBodyType && doc.responseBodyType.oneLineText !== 'void') {
    rows.push(row('Response body type', <TypeRefWithMime type={doc.responseBodyType} mimeTypes={doc.produces}/>));
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
