// @flow
import * as React from 'react';
import type { ApiAuthDoc } from '../../../../model/livedoc';
import { CodeElementsList } from '../../../shared/content/CodeElementsList';
import { HorizontalList } from '../../../shared/content/HorizontalList';

export type AuthInfoProps = {
  auth: ApiAuthDoc,
}

export const AuthInfo = ({auth}: AuthInfoProps) => {
  return <div>
    Type: <code>{auth.type}</code><Scheme scheme={auth.scheme}/><br/>
    Roles: <CodeElementsList items={auth.roles}/>
  </div>;
};

const Scheme = ({scheme}) => {
  if (!scheme) {
    return null;
  }
  return [' – Scheme: ', <code>{scheme}</code>];
};

function element(header: string, data) {
  return <span>{header}: {data}</span>;
}
