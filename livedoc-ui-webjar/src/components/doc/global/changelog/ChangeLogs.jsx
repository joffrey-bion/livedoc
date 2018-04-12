// @flow
import * as React from 'react';
import type { ApiChangelogsDoc } from '../../../../model/livedoc';
import { ContentHeader } from '../../../shared/content/ContentHeader';
import { ChangeLog } from './ChangeLog';

export type ChangeLogsProps = {
  changeLogs: ?ApiChangelogsDoc,
}

export const ChangeLogs = ({changeLogs}: ChangeLogsProps) => {
  if (changeLogs == null) {
    return null;
  }
  const changeLogsElts = changeLogs.changelogs.map(cl => <ChangeLog key={cl.livedocId} changeLog={cl}/>);

  return <div>
    <ContentHeader title='Change Log'/>
    {changeLogsElts}
  </div>;
};
