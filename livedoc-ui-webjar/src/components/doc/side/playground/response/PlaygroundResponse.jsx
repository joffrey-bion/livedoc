// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Alert, Card } from 'reactstrap';
import type { ResponseMetaData } from '../../../../../model/playground';
import type { State } from '../../../../../model/state';
import { actions as playgroundActions } from '../../../../../redux/actions/playground';
import { PlaygroundResponseBody } from './PlaygroundResponseBody';

type PlaygroundResponseProps = {
  waitingResponse: boolean,
  streamingResponse: boolean,
  responseMeta: ?ResponseMetaData,
  responseBody: ?string,
  error: any,
}

const PlaygroundResponseDumb = (props: PlaygroundResponseProps) => {
  if (props.waitingResponse) {
    return <Alert color='primary'>Waiting for response...</Alert>;
  }
  if (props.error) {
    console.error('Here is the error', props.error);
    return <Alert color='danger'>An error occurred</Alert>;
  }
  if (!props.responseMeta) {
    return null;
  }
  const contentType = props.responseMeta.headers.get('Content-Type');
  return <Card style={{marginTop: '2rem', padding: '0.8rem'}}>
    <h4>Response</h4>
    <div>Status: {props.responseMeta.status}</div>
    <BodyContainer loading={props.streamingResponse} body={props.responseBody} type={contentType}/>
  </Card>;
};

const BodyContainer = ({loading, body, type}) => {
  if (loading) {
    return <div>Loading response body...</div>;
  }
  return <PlaygroundResponseBody body={body} contentType={type}/>;
};

const mapStateToProps = (state: State) => ({
  waitingResponse: state.playground.waitingResponse,
  streamingResponse: state.playground.streamingResponse,
  responseMeta: state.playground.responseMeta,
  responseBody: state.playground.responseBody,
  error: state.playground.error,
});

const mapDispatchToProps = {
  submitRequest: playgroundActions.sendRequest,
};

export const PlaygroundResponse = withRouter(connect(mapStateToProps, mapDispatchToProps)(PlaygroundResponseDumb));
