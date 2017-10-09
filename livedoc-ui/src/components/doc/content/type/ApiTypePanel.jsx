// @flow
import * as React from 'react';
import { PageHeader } from 'react-bootstrap';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import { EnumTypeDetails } from './enum/EnumTypeDetails';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';

type Props = {
  typeDoc: ApiObjectDoc,
  onSelect: (id: string) => void,
}

export const ApiTypePanel = ({typeDoc, onSelect}: Props) => {
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  const info = isEnum ? <EnumTypeDetails typeDoc={typeDoc}/> : <ComplexTypeDetails typeDoc={typeDoc} onSelect={onSelect}/>;

  return <section>
    <PageHeader>{typeDoc.name}</PageHeader>
    <p>{typeDoc.description}</p>
    {info}
  </section>
};
