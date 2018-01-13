// @flow
import * as React from 'react';
import type { ApiMethodDoc, LivedocID } from '../../../../model/livedoc';
import { RouteHelper } from '../../../../routing/routeHelpler';
import { MethodPanel } from './MethodPanel';

export type ApiMethodPanelProps = {
  methodDoc: ApiMethodDoc,
  parentApiId: LivedocID,
  open: boolean,
}

export const ApiMethodPanel = ({methodDoc, open, parentApiId}: ApiMethodPanelProps) => {

  const apiUrl = RouteHelper.apiUrl(parentApiId);
  const methodUrl = RouteHelper.methodUrl(parentApiId, methodDoc.livedocId);

  return <MethodPanel methodDoc={methodDoc} open={open} collapseUrl={apiUrl} expandUrl={methodUrl}/>;
};
