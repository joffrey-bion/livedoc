// @flow
import * as React from 'react';

export type EnumValueRowProps = {
  value: string, description: string,
}

export const EnumValueRow = (props: EnumValueRowProps) => {
  return <tr>
    <td><code>{props.value}</code></td>
    <td>{props.description}</td>
  </tr>;
};
