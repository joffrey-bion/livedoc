// @flow
import * as React from 'react';
import { Badge } from 'reactstrap';
import './StageBadge.css';
import type { ApiStage } from '../../../model/livedoc';

export type StageBadgeProps = {
  stage: ?ApiStage,
}

export const StageBadge = ({stage}: StageBadgeProps) => {
  if (!stage) {
    return null;
  }
  return <Badge className="stageBadge" pill>{stage}</Badge>;
};
