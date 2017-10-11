// @flow
import * as React from 'react';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { EnumTypeDetails } from './enum/EnumTypeDetails';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';
import { ContentHeader } from '../ContentHeader';

export type ApiTypePanelProps = {
  typeDoc: ApiObjectDoc,
  onTypeClick: (id: string) => void,
}

const TypeDocContent = ({isEnum, typeDoc, onTypeClick}) => {
  if (isEnum) {
    return <EnumTypeDetails typeDoc={typeDoc}/>;
  } else {
    return <ComplexTypeDetails typeDoc={typeDoc} onTypeClick={onTypeClick}/>
  }
};

export const TypeDocPanel = ({typeDoc, onTypeClick}: ApiTypePanelProps) => {
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;

  return <section>
    <ContentHeader title={typeDoc.name} description={typeDoc.description}/>
    <TypeDocContent isEnum={isEnum} typeDoc={typeDoc} onTypeClick={onTypeClick}/>
  </section>
};
