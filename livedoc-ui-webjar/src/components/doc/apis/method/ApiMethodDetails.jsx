// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import { ApiMethodParamsTable } from './params/ApiMethodParamsTable';
import { TypeRefWithMime } from './TypeRefWithMime';

export type ApiMethodDetailsProps = {
  methodDoc: ApiMethodDoc,
}

export const ApiMethodDetails = (props: ApiMethodDetailsProps) => {
  const doc: ApiMethodDoc = props.methodDoc;

  let rows = [];
  if (doc.pathparameters && doc.pathparameters.length > 0) {
    rows.push(row('Path Params', <ApiMethodParamsTable params={doc.pathparameters}/>));
  }
  if (doc.queryparameters && doc.queryparameters.length > 0) {
    rows.push(row('Query Params', <ApiMethodParamsTable params={doc.queryparameters}/>));
  }
  if (doc.bodyobject) {
    rows.push(row('Request body type', <TypeRefWithMime type={doc.bodyobject.type} mimeTypes={doc.consumes}/>));
  }
  if (doc.response && doc.response.type && doc.response.type.oneLineText !== 'void') {
    rows.push(row('Response body type', <TypeRefWithMime type={doc.response.type} mimeTypes={doc.produces}/>));
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
