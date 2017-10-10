// @flow
import * as React from 'react';
import { PageHeader } from 'react-bootstrap';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { EnumTypeDetails } from './enum/EnumTypeDetails';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';
import { ContentHeader } from '../ContentHeader';

export type ApiTypePanelProps = {
  typeDoc: ApiObjectDoc,
  onSelect: (id: string) => void,
}

export const TypeDocPanel = ({typeDoc, onSelect}: ApiTypePanelProps) => {
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  const info = isEnum ? <EnumTypeDetails typeDoc={typeDoc}/> : <ComplexTypeDetails typeDoc={typeDoc} onSelect={onSelect}/>;

  return <section>
    <ContentHeader title={typeDoc.name} description={typeDoc.description}/>
    {info}
  </section>
};
