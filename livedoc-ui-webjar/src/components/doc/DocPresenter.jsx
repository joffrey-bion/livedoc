// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import type { State } from '../../model/state';
import { isDocLoaded } from '../../redux/doc';
import { ApisScene } from './apis/ApisScene';
import { GlobalDocScene } from './global/GlobalDocScene';
import { TypesScene } from './types/TypesScene';

export type DocPresenterProps = {
  docPresent: boolean
}

const DocPresenter = ({docPresent}: DocPresenterProps) => {
  if (!docPresent) {
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
      <Redirect to="/global"/>
    </Switch>
  </section>;
};

const renderApi = ({match}) => <ApisScene selectedApiId={match.params.apiId} selectedMethodId={match.params.methodId}/>;

const renderType = ({match}) => <TypesScene selectedTypeId={match.params.typeId}/>;

const mapStateToProps = (state: State) => ({
  docPresent: isDocLoaded(state),
});

const mapDispatchToProps = {};

export const Doc = withRouter(connect(mapStateToProps, mapDispatchToProps)(DocPresenter));

