// @flow
import * as React from 'react';
import { Panel } from 'react-bootstrap';
import type { ApiFlowDoc } from '../../../../model/livedoc';

type Props = {
  flowDoc: ApiFlowDoc,
}

export const ApiFlowPanel = (props: Props) => {
  const flow: ApiFlowDoc = props.flowDoc;

  return <Panel header={flow.name}>
    {flow.description}
  </Panel>;
};

