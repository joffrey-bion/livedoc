// @flow
import * as React from 'react';
import type { ApiOperationDoc, LivedocID } from '../../../../model/livedoc';
import { RouteHelper } from '../../../../routing/routeHelpler';
import { OperationPanel } from './OperationPanel';

export type ApiOperationPanelProps = {
  operationDoc: ApiOperationDoc,
  parentApiId: LivedocID,
  open: boolean,
}

export const ApiOperationPanel = ({operationDoc, open, parentApiId}: ApiOperationPanelProps) => {

  const apiUrl = RouteHelper.apiUrl(parentApiId);
  const operationUrl = RouteHelper.operationUrl(parentApiId, operationDoc.livedocId);

  return <OperationPanel operationDoc={operationDoc} open={open} collapseUrl={apiUrl} expandUrl={operationUrl}/>;
};
