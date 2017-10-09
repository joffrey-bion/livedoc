// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../../model/livedoc';
import { TypeInfo } from '../../common/TypeInfo';

export type ApiFieldRowProps = {
  field: ApiObjectFieldDoc,
  onSelect: (id: string) => void,
}

export const ApiFieldRow = (props: ApiFieldRowProps) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeInfo type={field.type} onSelect={props.onMethodSelect}/></td>
    <td>{field.description}</td>
  </tr>;
};
