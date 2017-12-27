// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getApi } from '../../../../redux/livedoc';
import { ContentHeader } from '../ContentHeader';
import { ApiMethodPanel } from './ApiMethodPanel';

export type ApiDocPanelOwnProps = {
  match: any,
  location: any,
}

export type ApiDocPanelProps = ApiDocPanelOwnProps & {
  apiDoc: ?ApiDoc,
}

const ApiDocPanel = ({apiDoc, ...props}: ApiDocPanelProps) => {
  if (!apiDoc) {
    return <Redirect to="/apis"/>;
  }
  const api: ApiDoc = apiDoc;

  const methodPanels = api.methods.map(m => <ApiMethodPanel key={m.livedocId} methodDoc={m} {...props}/>);

  return <section>
    <ContentHeader title={api.name} description={api.description}/>
    {methodPanels}
  </section>;
};

const mapStateToProps = (state: State, props: ApiDocPanelOwnProps) => ({
  apiDoc: getApi(props.match.params.apiId, state),
  ...props,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(ApiDocPanel);


