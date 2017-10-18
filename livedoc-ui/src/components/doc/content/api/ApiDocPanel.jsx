// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getApi } from '../../../../redux/reducer';
import { ContentHeader } from '../ContentHeader';
import { ApiMethodPanel } from './ApiMethodPanel';
import { Redirect } from 'react-router-dom';

export type ApiDocPanelProps = {
  apiDoc: ?ApiDoc,
}

const ApiDocPanel = ({apiDoc}: ApiDocPanelProps) => {
  if (!apiDoc) {
    return <Redirect to="/apis"/>
  }
  const api: ApiDoc = apiDoc;

  const methodPanels = api.methods.map(m => <ApiMethodPanel key={m.id} methodDoc={m}/>);

  return <section>
    <ContentHeader title={api.name} description={api.description}/>
    {methodPanels}
  </section>;
};

export type ApiDocPanelOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: ApiDocPanelOwnProps) => ({
  apiDoc: getApi(match.params.apiId, state),
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(ApiDocPanel);


