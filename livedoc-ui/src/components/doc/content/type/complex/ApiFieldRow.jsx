// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../../model/livedoc';
import { TypeInfo } from '../../common/TypeInfo';

type Props = {
  field: ApiObjectFieldDoc,
  onSelect: (id: string) => void,
}

export const ApiFieldRow = (props: Props) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeInfo type={field.type} onSelect={props.onSelect}/></td>
    <td>{field.description}</td>
  </tr>;
};
