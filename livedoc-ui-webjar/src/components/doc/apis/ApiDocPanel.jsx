// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiDoc, ApiOperationDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getApi } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelper';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { MessagePanel } from './message/MessagePanel';
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
  const operationPanels = api.operations.map((op: ApiOperationDoc) => {
    const open = op.livedocId === selectedOperationId;
    return <ApiOperationPanel key={op.livedocId} operationDoc={op} open={open} parentApiId={api.livedocId}/>;
  });
  const messagePanels = api.messages.map(op => {
    const open = op.livedocId === selectedOperationId;
    return <MessagePanel key={op.livedocId} messageDoc={op} open={open} parentApiId={api.livedocId}/>;
  });

  return <section>
    <ContentHeader title={api.name} description={api.description} stage={api.stage} />
    {operationPanels}
    {operationPanels.length > 0 && messagePanels.length > 0 && <OperationsMessagesSeparator/>}
    {messagePanels}
  </section>;
};

const OperationsMessagesSeparator = () => <p style={{marginBottom: "0.5rem"}}>
  <b>Websocket messages</b>
</p>;

const mapStateToProps = (state: State, {apiId, selectedOperationId}: ApiDocPanelOwnProps) => ({
  apiDoc: apiId && getApi(apiId, state),
  selectedOperationId: selectedOperationId,
});

const mapDispatchToProps = {};

export const ApiDocPanel = connect(mapStateToProps, mapDispatchToProps)(ApiDocPanelPresenter);


