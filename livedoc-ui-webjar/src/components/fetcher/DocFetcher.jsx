// @flow
import { CircularProgress } from 'material-ui';
import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Alert, Container, Row } from 'reactstrap';
import type { State } from '../../model/state';
import { actions } from '../../redux/actions/loader';
import { InlineForm } from './InlineForm';

type Props = {
  loading: boolean,
  loadingError: ?string,
  url: string,
  fetchDoc: (url: string) => void,
}

const absoluteCenter = {
  opacity: 1,
  position: 'absolute',
  margin: 'auto',
  top: 0,
  right: 0,
  bottom: 0,
  left: 0,
};

const Form = ({fetchDoc, url, ...props}) => <InlineForm hintText='URL to JSON documentation' btnLabel='Get Doc'
                                                   initialValue={url}
                                                   onSubmit={fetchDoc} {...props}/>;
const FetchError = ({loadingError}) => {
  if (loadingError === null) {
    return null;
  }
  return <Alert color="danger">{loadingError}</Alert>;
};

const DocFetcherPresenter = (props: Props) => {
  if (props.loading) {
    return <CircularProgress style={absoluteCenter} size={370} thickness={0.4}/>;
  } else {
    return <Container style={{height: '10rem'}}>
      <Row className="h-100 align-items-center">
        <Form {...props}/>
      </Row>
      <FetchError loadingError={props.loadingError}/>
    </Container>;
  }
};

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  loadingError: state.loader.loadingError,
  url: state.loader.url,
});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
};

export const DocFetcher = withRouter(connect(mapStateToProps, mapDispatchToProps)(DocFetcherPresenter));
