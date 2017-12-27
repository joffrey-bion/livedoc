// @flow
import * as React from 'react';
import type { TypeInfoProps } from '../common/TypeInfo';
import { TypeInfo } from '../common/TypeInfo';

export type TypeInfoWithMimeProps = TypeInfoProps & {
  mimeTypes: ?(string[]),
}

export const TypeInfoWithMime = ({mimeTypes, ...props}: TypeInfoWithMimeProps) => {

  const typeInfo = <TypeInfo {...props}/>;

  if (mimeTypes) {
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
