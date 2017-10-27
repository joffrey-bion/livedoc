// @flow
import * as React from 'react';
import type { ApiChangelogsDoc } from '../../../../../model/livedoc';
import { ContentHeader } from '../../ContentHeader';
import { ChangeLog } from './ChangeLog';

export type ChangeLogsProps = {
  changeLogs: ApiChangelogsDoc,
}

export const ChangeLogs = (props: ChangeLogsProps) => {

  const changeLogs = props.changeLogs.changelogs.map(cl => <ChangeLog key={cl.livedocId} changeLog={cl}/>);

  return <div>
    <ContentHeader title='Change Log'/>
    {changeLogs}
  </div>;
};
