// @flow
import * as React from 'react';
import type { ApiObjectFieldDoc } from '../../../../model/livedoc';

type Props = {
  field: ApiObjectFieldDoc
}

export const ApiFieldRow = (props: Props) => {
  const field: ApiObjectFieldDoc = props.field;

  return <tr>
    <td>{field.name}</td>
    <td>{field.type.oneLineText}</td>
    <td>{field.description}</td>
  </tr>;
};
