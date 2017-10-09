// @flow
import * as React from 'react';
import type { ApiMethodDoc } from '../../../../model/livedoc';

type Props = {
  method: ApiMethodDoc
}

export const ApiMethodPanel = (props: Props) => {
  const method: ApiMethodDoc = props.method;

  return <tr>
    <td>{method.method}</td>
    <td>{method.description}</td>
  </tr>;
};
