// @flow
import * as React from 'react';
import type { ApiMigrationsDoc } from '../../../../../model/livedoc';
import { ContentHeader } from '../../ContentHeader';
import { Migration } from './Migration';

export type MigrationsProps = {
  migrations: ApiMigrationsDoc,
}

export const Migrations = ({migrations}: MigrationsProps) => {

  const migs = migrations.migrations.map(m => <Migration key={m.livedocId} migration={m}/>);

  return <div>
    <ContentHeader title='Migrations'/>
    {migs}
  </div>;
};
