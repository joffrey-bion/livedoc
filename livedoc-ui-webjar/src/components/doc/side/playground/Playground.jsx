// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Card } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { actions as playgroundActions } from '../../../../redux/actions/playground';
import { getMethod } from '../../../../redux/livedoc';
import { PlaygroundForm } from './PlaygroundForm';
import { PlaygroundResponse } from './response/PlaygroundResponse';

export type PlaygroundProps = {
  basePath: string,
  methodDoc: ApiMethodDoc,
  submitRequest: (request: RequestInfo) => void,
}

const Playground = ({basePath, methodDoc, submitRequest}: PlaygroundProps) => {
  if (!methodDoc) {
    return <p>Select a method to enable the playground</p>;
  }

  return <section>
    <h3 style={{marginTop: '1rem'}}>Playground</h3>
    <Card style={{padding: '0.8rem'}}>
      <PlaygroundForm basePath={basePath} methodDoc={methodDoc} onSubmit={submitRequest}/>
    </Card>
    <PlaygroundResponse/>
  </section>;
};

type PlaygroundOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: PlaygroundOwnProps) => ({
  basePath: state.livedoc && state.livedoc.basePath,
  methodDoc: getMethod(match.params.apiId, match.params.methodId, state),
});

const mapDispatchToProps = {
  submitRequest: playgroundActions.sendRequest,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Playground));
