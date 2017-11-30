// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Card } from 'reactstrap';
import type { ApiMethodDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getMethod } from '../../../../redux/livedoc';
import { PlaygroundForm } from './PlaygroundForm';
import { actions as playgroundActions } from "../../../../redux/actions/playground";
import { PlaygroundResponse } from "./PlaygroundResponse";

export type PlaygroundProps = {
  basePath: string,
  methodDoc: ApiMethodDoc,
  submitRequest: (request: RequestInfo) => void,
  playgroundResponse: Response,
}

const Playground = ({basePath, methodDoc, submitRequest, playgroundResponse}: PlaygroundProps) => {
  if (!methodDoc) {
    return <p>Select a method to enable the playground</p>;
  }

  return <section>
    <h3 style={{marginTop: '1rem'}}>Playground</h3>
    <Card style={{padding: '0.8rem'}}>
      <PlaygroundForm basePath={basePath} methodDoc={methodDoc} onSubmit={submitRequest}/>
      <PlaygroundResponse response={playgroundResponse}/>
    </Card>
  </section>;
};

export type PlaygroundOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: PlaygroundOwnProps) => ({
  basePath: state.livedoc && state.livedoc.basePath,
  methodDoc: getMethod(match.params.apiId, match.params.methodId, state),
  playgroundResponse: state.playground.response,
});

const mapDispatchToProps = {
  submitRequest: playgroundActions.sendRequest
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Playground));
