// @flow
import * as React from 'react';
import type { ApiChangelogDoc } from '../../../../../model/livedoc';

export type ChangeLogProps = {
  changeLog: ApiChangelogDoc,
}

export const ChangeLog = (props: ChangeLogProps) => {

  const changes = props.changeLog.changes.map(change => <li key={change}>{change}</li>);

  return <section>
    <h2>Version {props.changeLog.version}</h2>
    <ul>
    {changes}
    </ul>
  </section>;
};
