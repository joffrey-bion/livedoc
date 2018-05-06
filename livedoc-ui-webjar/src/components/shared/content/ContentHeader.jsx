// @flow
import * as React from 'react';
import type { ApiStage } from '../../../model/livedoc';
import { Html } from './Html';
import { StageBadge } from './StageBadge';

export type ContentHeaderProps = {
  title: string,
  description?: string,
  stage?: ?ApiStage,
}

export const ContentHeader = ({title, description, stage}: ContentHeaderProps) => {
  return <div>
    <h1>{title} <StageBadge stage={stage}/></h1>
    <hr/>
    {description && <p className="lead"><Html content={description}/></p>}
  </div>;
};
