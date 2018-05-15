// @flow
import * as React from 'react';
import type { ApiPropertyDoc } from '../../../../model/livedoc';
import { TypeRef } from '../../typeref/TypeRef';

export type TypeFieldRowProps = {
  field: ApiPropertyDoc,
}

export const TypeFieldRow = (props: TypeFieldRowProps) => {
  const field: ApiPropertyDoc = props.field;
  const desc = field.description && <p>{field.description}</p>;
  return <tr>
    <td><code>{field.name}</code></td>
    <td><TypeRef type={field.type}/></td>
    <td>{desc}<Format constraints={field.format}/></td>
  </tr>;
};

type FormatProps = {
  constraints: Array<string>,
}

const Format = ({constraints}: FormatProps) => {
  if (constraints.length === 0) {
    return null;
  }
  if (constraints.length === 1) {
    return [<h6>Format:</h6>, constraints[0]];
  }
  const items = constraints.map((c, index) => <li key={index}>{c}</li>);
  const list = <ul>{items}</ul>;
  return [<h6>Format:</h6>, list];
};
