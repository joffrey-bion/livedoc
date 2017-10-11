// @flow
import * as React from 'react';
import type { ApiObjectDoc } from '../../../../model/livedoc';

export type TypeTemplateProps = {
  typeDoc: ApiObjectDoc,
}

export const TypeTemplate = ({typeDoc}: TypeTemplateProps) => {
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  if (isEnum) {
    return null;
  }
  return <section>
    <h2>Template</h2>
    <pre>
    {JSON.stringify(typeDoc.template, null, 2)}
    </pre>
  </section>
};
