// @flow
import * as React from 'react';
import type { ApiOperationDoc } from '../../../model/livedoc';
import { RouteHelper } from '../../../routing/routeHelpler';
import { OperationPanel } from '../apis/method/OperationPanel';

export type FlowOperationPanelProps = {
  operationDoc: ApiOperationDoc,
  open: boolean,
}

export const FlowOperationPanel = ({operationDoc, open}: FlowOperationPanelProps) => {

  const flowsUrl = RouteHelper.flowsUrl();
  const operationUrl = RouteHelper.flowOperationUrl(operationDoc.livedocId);

  return <OperationPanel operationDoc={operationDoc} open={open} collapseUrl={flowsUrl} expandUrl={operationUrl}/>;
};
