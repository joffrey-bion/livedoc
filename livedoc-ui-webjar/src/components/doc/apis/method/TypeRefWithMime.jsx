// @flow
import * as React from 'react';
import { CodeElementsList } from '../../../shared/content/CodeElementsList';
import type { TypeRefProps } from '../../typeref/TypeRef';
import { TypeRef } from '../../typeref/TypeRef';

export type TypeRefWithMimeProps = TypeRefProps & {
  mimeTypes: Array<string>,
}

export const TypeRefWithMime = ({mimeTypes, ...props}: TypeRefWithMimeProps) => {

  const typeInfo = <TypeRef {...props}/>;

  if (mimeTypes.length > 0) {
    return <span>{typeInfo} as <CodeElementsList items={mimeTypes}/></span>;
  }
  return typeInfo;
};
