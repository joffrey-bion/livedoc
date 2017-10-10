// @flow
import * as React from 'react';
import { PageHeader } from 'react-bootstrap';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { EnumTypeDetails } from './enum/EnumTypeDetails';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';

export type ApiTypePanelProps = {
  typeDoc: ApiObjectDoc,
  onSelect: (id: string) => void,
}

export const TypeDocPanel = ({typeDoc, onSelect}: ApiTypePanelProps) => {
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  const info = isEnum ? <EnumTypeDetails typeDoc={typeDoc}/> : <ComplexTypeDetails typeDoc={typeDoc} onSelect={onSelect}/>;

  return <section>
    <PageHeader>{typeDoc.name}</PageHeader>
    <p>{typeDoc.description}</p>
    {info}
  </section>
};
