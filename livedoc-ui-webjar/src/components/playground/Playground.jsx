// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Card } from 'reactstrap';
import type { ApiOperationDoc, LivedocID } from '../../model/livedoc';
import type { State } from '../../model/state';
import { actions as playgroundActions } from '../../redux/actions/playground';
import { getMethod } from '../../redux/livedoc';
import { PlaygroundForm } from './PlaygroundForm';
import { PlaygroundResponse } from './response/PlaygroundResponse';

export type PlaygroundProps = {
  basePath: string,
  operationDoc: ?ApiOperationDoc,
  submitRequest: (request: RequestInfo) => void,
}

const Playground = ({basePath, operationDoc, submitRequest}: PlaygroundProps) => {
  if (!operationDoc) {
    return <p>Select a method to enable the playground</p>;
  }

  return <section>
    <h3 style={{marginTop: '1rem'}}>Playground</h3>
    <Card style={{padding: '0.8rem'}}>
      <PlaygroundForm basePath={basePath} operationDoc={operationDoc} onSubmit={submitRequest}/>
    </Card>
    <PlaygroundResponse/>
  </section>;
};

type PlaygroundOwnProps = {
  selectedApiId: ?LivedocID,
  selectedOperationId: ?LivedocID,
}

const mapStateToProps = (state: State, {selectedApiId, selectedOperationId}: PlaygroundOwnProps) => ({
  basePath: state.livedoc && state.livedoc.basePath,
  operationDoc: selectedApiId && selectedOperationId && getMethod(selectedApiId, selectedOperationId, state),
});

const mapDispatchToProps = {
  submitRequest: playgroundActions.sendRequest,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Playground));
