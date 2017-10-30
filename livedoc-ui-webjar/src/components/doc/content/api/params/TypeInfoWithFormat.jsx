// @flow
import * as React from 'react';
import type { TypeInfoProps } from '../../common/TypeInfo';
import { TypeInfo } from '../../common/TypeInfo';

export type TypeInfoWithFormatProps = TypeInfoProps & {
  format: ?string,
}

export const TypeInfoWithFormat = ({format, ...props}: TypeInfoWithFormatProps) => {

  const typeInfo = <TypeInfo {...props} />;

  if (format) {
    return <span>{typeInfo} (format: {format})</span>
  }
  return typeInfo;
};
