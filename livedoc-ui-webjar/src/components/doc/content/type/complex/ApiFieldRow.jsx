// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../../model/livedoc';
import { TypeRef } from '../../common/TypeRef';

export type ApiFieldRowProps = {
  field: ApiObjectFieldDoc,
}

export const ApiFieldRow = (props: ApiFieldRowProps) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeRef type={field.type}/></td>
    <td>{field.description}</td>
  </tr>;
};
