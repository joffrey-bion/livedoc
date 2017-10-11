// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../../model/livedoc';
import { TypeInfo } from '../../common/TypeInfo';

export type ApiFieldRowProps = {
  field: ApiObjectFieldDoc,
  onTypeClick: (id: string) => void,
}

export const ApiFieldRow = (props: ApiFieldRowProps) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeInfo type={field.type} onTypeClick={props.onTypeClick}/></td>
    <td>{field.description}</td>
  </tr>;
};
