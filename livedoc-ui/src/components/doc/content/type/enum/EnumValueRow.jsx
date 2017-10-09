// @flow
import * as React from 'react';

type Props = {
  value: string, description: string,
}

export const EnumValueRow = (props: Props) => {
  return <tr>
    <td><code>{props.value}</code></td>
    <td>{props.description}</td>
  </tr>;
};
