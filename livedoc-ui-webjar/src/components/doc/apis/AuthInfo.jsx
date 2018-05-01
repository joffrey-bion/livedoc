// @flow
import * as React from 'react';
import type { ApiAuthDoc } from '../../../model/livedoc';
import { CodeElementsList } from '../../shared/content/CodeElementsList';

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
