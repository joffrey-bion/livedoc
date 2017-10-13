// @flow
import * as React from 'react';
import { Table } from 'reactstrap';
import type { ApiMethodDoc, LivedocID } from '../../../../model/livedoc';
import { TypeInfoWithMime } from './TypeInfoWithMime';
import { ApiMethodParamsTable } from './params/ApiMethodParamsTable';

export type ApiMethodDetailsProps = {
  methodDoc: ApiMethodDoc,
  onTypeClick: (id: LivedocID) => void,
}

export const ApiMethodDetails = (props: ApiMethodDetailsProps) => {
  const doc: ApiMethodDoc = props.methodDoc;

  let rows = [];
  if (doc.pathparameters && doc.pathparameters.length > 0) {
    rows.push(row('Path Params', <ApiMethodParamsTable params={doc.pathparameters}
                                                       onTypeClick={props.onTypeClick}/>))
  }
  if (doc.queryparameters && doc.queryparameters.length > 0) {
    rows.push(row('Query Params', <ApiMethodParamsTable params={doc.queryparameters}
                                                        onTypeClick={props.onTypeClick}/>))
  }
  if (doc.bodyobject) {
    rows.push(row('Request body type', <TypeInfoWithMime type={doc.bodyobject.type}
                                                 mimeTypes={doc.consumes}
                                                 onTypeClick={props.onTypeClick}/>))
  }
  if (doc.response && doc.response.type && doc.response.type.oneLineText !== 'void') {
    rows.push(row('Response body type', <TypeInfoWithMime type={doc.response.type}
                                                     mimeTypes={doc.produces}
                                                     onTypeClick={props.onTypeClick}/>))
  }

  if (rows.length === 0) {
    if (doc.description) {
      return null;
    } else {
      return <small className="text-muted">No additional information available</small>
    }
  }

  return <Table striped>
    <tbody>
    {rows}
    </tbody>
  </Table>;
};

function row(header: string, data) {
  return <tr>
    <th scope="row">{header}</th>
    <td>{data}</td>
  </tr>;
}
