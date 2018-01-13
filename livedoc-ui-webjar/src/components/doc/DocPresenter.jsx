// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import type { Livedoc } from '../../model/livedoc';
import type { State } from '../../model/state';
import { ApisScene } from './apis/ApisScene';
import { FlowsScene } from './flows/FlowsScene';
import { GlobalDocScene } from './global/GlobalDocScene';
import { TypesScene } from './types/TypesScene';

export type DocPresenterProps = {
  loading: boolean,
  url: ?string,
  livedoc: ?Livedoc
}

const DocPresenter = (props: DocPresenterProps) => {
  if (!props.livedoc) {
    return <p>Please provide a URL to fetch a documentation.</p>;
  }

  return <section className='App-content'>
    <Switch>
      <Route path="/global" component={GlobalDocScene}/>
      <Route exact path="/apis" render={renderApi}/>
      <Route exact path="/apis/:apiId" render={renderApi}/>
      <Route path="/apis/:apiId/:methodId" render={renderApi}/>
      <Route exact path="/types" render={renderType}/>
      <Route path="/types/:typeId" render={renderType}/>
      <Route exact path="/flows" render={renderFlow}/>
      <Route path="/flows/:flowId" render={renderFlow}/>
      <Redirect to="/global"/>
    </Switch>
  </section>;
};

const renderApi = ({match}) => <ApisScene selectedApiId={match.params.apiId} selectedMethodId={match.params.methodId}/>;

const renderType = ({match}) => <TypesScene selectedTypeId={match.params.typeId}/>;

const renderFlow = ({match}) => <FlowsScene selectedFlowId={match.params.flowId}
                                            selectedMethodId={match.params.methodId}/>;

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  url: state.loader.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export const Doc = withRouter(connect(mapStateToProps, mapDispatchToProps)(DocPresenter));

