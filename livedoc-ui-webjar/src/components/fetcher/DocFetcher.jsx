// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Alert, Col, Container, Row } from 'reactstrap';
import type { State } from '../../model/state';
import { actions } from '../../redux/actions/loader';
import { FilePickerButton } from '../shared/button/FilePickerButton';
import { InlineForm } from './InlineForm';

type DocFetcherProps = {
  loadingError: ?string,
  initialUrl: string,
  fetchDoc: (url: string) => void,
  loadFile: (file: File) => void,
}

const FetchError = ({loadingError}) => {
  if (loadingError === null) {
    return null;
  }
  return <Alert color="danger">{loadingError}</Alert>;
};

const DocFetcherPresenter = ({loadingError, initialUrl, loadFile, fetchDoc, ...other}: DocFetcherProps) => {
  return <Container style={{height: '10rem'}}>
    <Row className="h-100 align-items-center">
      <Col>
        <InlineForm btnLabel='Fetch Doc'
                    btnTitle='Fetch the doc as JSON from the given URL'
                    hintText='URL to JSON documentation'
                    initialValue={initialUrl}
                    onSubmit={fetchDoc} {...other}/>
      </Col>
      <Col md={{size: 'auto'}}>
        <FilePickerButton label="Import file"
                          title="Load the doc from a local JSON file"
                          color="info"
                          onChange={loadFile}/>
      </Col>
    </Row>
    <FetchError loadingError={loadingError}/>
  </Container>;
};

const mapStateToProps = (state: State) => ({
  loadingError: state.loader.loadingError,
  initialUrl: computeInitialUrl(state),
});

function computeInitialUrl(state: State): string {
  // 1. loading URL, because the only way we still have one is if there is an error, and in this case we want the same
  // URL so that the user can fix it

  // 2. the query, because if the user provided the URL param, we need to take it into account no matter what we have
  // already loaded (maybe he opened a link and we have another doc in local storage)

  // 3. the loaded doc's URL, because it's the last thing that's user-specific before a generic fallback

  // 4. generic fallback:
  // if using the webjar, then the doc endpoint in on the same server at /jsondoc, so "/jsondoc" is sufficient
  // if using an independent UI, then it is likely that the app server URL ends in /jsondoc, so this string helps anyway
  return state.loader.url || getDocUrlFromQuery() || (state.doc && state.doc.srcUrl) || '/jsondoc';
}

function getDocUrlFromQuery() {
  const url = new URL(window.location.href);
  return url.searchParams.get('url');
}

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
  loadFile: actions.loadDocFromFile,
};

export const DocFetcher = withRouter(connect(mapStateToProps, mapDispatchToProps)(DocFetcherPresenter));
