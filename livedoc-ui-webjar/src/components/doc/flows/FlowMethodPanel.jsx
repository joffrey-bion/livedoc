// @flow
import * as React from 'react';
import type { ApiMethodDoc } from '../../../model/livedoc';
import { RouteHelper } from '../../../routing/routeHelpler';
import { MethodPanel } from '../apis/method/MethodPanel';

export type FlowMethodPanelProps = {
  methodDoc: ApiMethodDoc,
  open: boolean,
}

export const FlowMethodPanel = ({methodDoc, open}: FlowMethodPanelProps) => {

  const flowsUrl = RouteHelper.flowsUrl();
  const methodUrl = RouteHelper.flowMethodUrl(methodDoc.livedocId);

  return <MethodPanel methodDoc={methodDoc} open={open} collapseUrl={flowsUrl} expandUrl={methodUrl}/>;
};
