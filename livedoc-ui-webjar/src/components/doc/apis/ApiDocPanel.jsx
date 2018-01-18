// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getApi } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelpler';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { ApiMethodPanel } from './method/ApiMethodPanel';

export type ApiDocPanelOwnProps = {
  apiId: ?LivedocID,
  selectedMethodId: ?LivedocID,
}

export type ApiDocPanelProps = {
  apiDoc: ?ApiDoc,
  selectedMethodId: ?LivedocID,
}

const ApiDocPanelPresenter = ({apiDoc, selectedMethodId}: ApiDocPanelProps) => {
  if (!apiDoc) {
    return <Redirect to={RouteHelper.apisUrl()}/>;
  }
  const api: ApiDoc = apiDoc;
  const methodPanels = api.methods.map(m => {
    const open = m.livedocId === selectedMethodId;
    return <ApiMethodPanel key={m.livedocId} methodDoc={m} open={open} parentApiId={api.livedocId}/>;
  });

  return <section>
    <ContentHeader title={api.name} description={api.description} stage={api.stage} />
    {methodPanels}
  </section>;
};

const mapStateToProps = (state: State, {apiId, selectedMethodId}: ApiDocPanelOwnProps) => ({
  apiDoc: apiId && getApi(apiId, state),
  selectedMethodId: selectedMethodId,
});

const mapDispatchToProps = {};

export const ApiDocPanel = connect(mapStateToProps, mapDispatchToProps)(ApiDocPanelPresenter);


