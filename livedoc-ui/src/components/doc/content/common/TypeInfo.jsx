// @flow
import * as React from 'react';
import type { LivedocType } from '../../../../model/livedoc';

type Props = {
  type: LivedocType,
  onSelect: (id: string) => void,
}

export const TypeInfo = ({type}: Props) => {
  return <code>{type.oneLineText}</code>;
};
