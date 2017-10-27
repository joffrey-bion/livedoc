// @flow
import * as React from 'react';
import type { ApiMigrationDoc } from '../../../../../model/livedoc';

export type MigrationProps = {
  migration: ApiMigrationDoc,
}

export const Migration = ({migration}: MigrationProps) => {

  const steps = migration.steps.map(s => <li key={s}>{s}</li>);

  return <section>
    <h2>Migrating from {migration.fromVersion} to {migration.toVersion}</h2>
    <ul>
      {steps}
    </ul>
  </section>;
};
