// @flow
import * as React from 'react';
import type { LivedocType } from '../../../../model/livedoc';

export type TypeInfoProps = {
  type: LivedocType,
  required: ?boolean,
}

export const TypeInfo = (props: TypeInfoProps) => {
  const optionalMark = props.required === false ? '?' : '';
  return <a href={"/"}><code>{props.type.oneLineText + optionalMark}</code></a>;
};
