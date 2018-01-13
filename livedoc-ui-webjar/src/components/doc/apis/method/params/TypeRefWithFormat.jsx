// @flow
import * as React from 'react';
import type { TypeRefProps } from '../../../typeref/TypeRef';
import { TypeRef } from '../../../typeref/TypeRef';

export type TypeRefWithFormatProps = TypeRefProps & {
  format: ?string,
}

export const TypeRefWithFormat = ({format, ...props}: TypeRefWithFormatProps) => {

  const typeInfo = <TypeRef {...props} />;

  if (format) {
    return <span>{typeInfo} (format: {format})</span>;
  }
  return typeInfo;
};
