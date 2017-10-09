// @flow
import * as React from 'react';
import type { LivedocType } from '../../../../model/livedoc';

export type TypeInfoProps = {
  type: LivedocType,
  required: ?boolean,
  onTypeClick: (selectedTypeId: string) => void,
}

export const TypeInfo = (props: TypeInfoProps) => {
  const optionalMark = props.required === false ? '?' : '';
  return <code>{props.type.oneLineText + optionalMark}</code>;
};
