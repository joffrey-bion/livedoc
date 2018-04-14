// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getApi } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelper';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { ApiOperationPanel } from './method/ApiOperationPanel';

export type ApiDocPanelOwnProps = {
  apiId: ?LivedocID,
  selectedOperationId: ?LivedocID,
}

export type ApiDocPanelProps = {
  apiDoc: ?ApiDoc,
  selectedOperationId: ?LivedocID,
}

const ApiDocPanelPresenter = ({apiDoc, selectedOperationId}: ApiDocPanelProps) => {
  if (!apiDoc) {
    return <Redirect to={RouteHelper.apisUrl()}/>;
  }
  const api: ApiDoc = apiDoc;
  const operationPanels = api.operations.map(op => {
    const open = op.livedocId === selectedOperationId;
    return <ApiOperationPanel key={op.livedocId} operationDoc={op} open={open} parentApiId={api.livedocId}/>;
  });

  return <section>
    <ContentHeader title={api.name} description={api.description} stage={api.stage} />
    {operationPanels}
  </section>;
};

const mapStateToProps = (state: State, {apiId, selectedOperationId}: ApiDocPanelOwnProps) => ({
  apiDoc: apiId && getApi(apiId, state),
  selectedOperationId: selectedOperationId,
});

const mapDispatchToProps = {};

export const ApiDocPanel = connect(mapStateToProps, mapDispatchToProps)(ApiDocPanelPresenter);


