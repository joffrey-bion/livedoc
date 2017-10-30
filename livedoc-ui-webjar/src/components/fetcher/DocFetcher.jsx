// @flow
import { CircularProgress } from 'material-ui';
import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Container, Row } from 'reactstrap';
import type { State } from '../../model/state';
import { isDocLoaded } from '../../redux/livedoc';
import { actions } from '../../redux/loader';
import { InlineForm } from '../forms/InlineForm';

type Props = {
  loading: boolean,
  url: ?string,
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

const Form = ({fetchDoc, ...props}) => <InlineForm hintText='URL to JSON documentation' btnLabel='Get Doc'
                                                   initialValue={computeInitialUrl()}
                                                   onSubmit={fetchDoc} {...props}/>;

const DocFetcher = (props: Props) => {
  if (props.loading && props.url) {
    return <CircularProgress style={absoluteCenter} size={370} thickness={0.4}/>;
  } else {
    return <Container style={{height: '10rem'}}>
      <Row className="h-100 align-items-center">
        <Form {...props}/>
      </Row>
    </Container>;
  }
};

const DEFAULT_FETCH_URL = 'http://localhost:8080/jsondoc';

function computeInitialUrl(): string {
  const url = new URL(window.location.href);
  const specifiedUrl = url.searchParams.get('url');
  if (specifiedUrl) {
    return specifiedUrl;
  }
  return DEFAULT_FETCH_URL;
}

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  url: state.loader.url,
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(DocFetcher));
