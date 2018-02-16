// @flow
import * as React from 'react';
import type { ApiAuthDoc } from '../../../../model/livedoc';
import { CodeElementsList } from '../../../shared/content/CodeElementsList';
import { HorizontalList } from '../../../shared/content/HorizontalList';

export type AuthInfoProps = {
  auth: ApiAuthDoc,
}

export const AuthInfo = ({auth}: AuthInfoProps) => {
  const elements = [];
  elements.push(element('Type', <code>{auth.type}</code>));
  elements.push(element('Roles', <CodeElementsList items={auth.roles}/>));
  if (auth.scheme) {
    elements.push(element('Scheme', <code>{auth.scheme}</code>));
  }

  return <HorizontalList items={elements}/>;
};

function element(header: string, data) {
  return <span>{header}: {data}</span>;
}
