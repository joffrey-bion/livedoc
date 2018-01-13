// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../model/livedoc';
import { TypeRef } from '../../typeref/TypeRef';

export type TypeFieldRowProps = {
  field: ApiObjectFieldDoc,
}

export const TypeFieldRow = (props: TypeFieldRowProps) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeRef type={field.type}/></td>
    <td>{field.description}</td>
  </tr>;
};
