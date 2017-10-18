// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getApi } from '../../../../redux/reducer';
import { ContentHeader } from '../ContentHeader';
import { ApiMethodPanel } from './ApiMethodPanel';

export type ApiDocPanelProps = {
  apiDoc: ApiDoc,
}

const ApiDocPanel = (props: ApiDocPanelProps) => {
  const api: ApiDoc = props.apiDoc;

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


