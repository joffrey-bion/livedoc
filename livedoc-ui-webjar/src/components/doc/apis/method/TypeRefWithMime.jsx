// @flow
import * as React from 'react';
import type { TypeRefProps } from '../../typeref/TypeRef';
import { TypeRef } from '../../typeref/TypeRef';

export type TypeRefWithMimeProps = TypeRefProps & {
  mimeTypes: Array<string>,
}

export const TypeRefWithMime = ({mimeTypes, ...props}: TypeRefWithMimeProps) => {

  const typeInfo = <TypeRef {...props}/>;

  if (mimeTypes.length > 0) {
    return <span>{typeInfo} as {joinAsCodeBlocks(mimeTypes, ', ')}</span>;
  }
  return typeInfo;
};

function joinAsCodeBlocks(elements: string[], delimiter: string) {
  const codeElems = elements.map(s => <code key={s}>{s}</code>);
  let result = [];
  for (let e of codeElems) {
    result.push(e);
    result.push(delimiter);
  }
  if (result.length > 0) {
    result.pop();
  }
  return result;
}
