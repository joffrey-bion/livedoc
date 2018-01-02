// @flow
import * as React from 'react';
import type { TypeRefProps } from '../../common/TypeRef';
import { TypeRef } from '../../common/TypeRef';

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
