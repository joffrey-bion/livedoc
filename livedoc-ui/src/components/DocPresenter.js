// @flow
import * as React from 'react';
import {connect} from 'react-redux';
import type {Livedoc, ApiDoc} from '../models/livedoc';
import type {State} from '../models/state';

type Props = {
  loading: boolean, url: ?string, livedoc: ?Livedoc
}

const DocPresenter = (props: Props) => {
  if (props.loading) {
    return <span>Loading documentation from {props.url}</span>
  }
  if (!props.livedoc) {
    return <span>Please provide a URL to fetch a documentation.</span>
  }
  const apiDocs: {[key: string]: Array<ApiDoc>} = props.livedoc.apis;
  // const nbApis: number = apiDocs.keys();
  return <div>
    <p>apiDocs.get? = {!!apiDocs.get}</p>
    <p>apiDocs.keys? = {!!apiDocs.keys}</p>
    <pre style={{textAlign: 'left'}}>apiDocs[""] = {JSON.stringify(apiDocs[''], null, 3)}</pre>
  </div>
};

const mapStateToProps = (state: State) => ({
  loading: state.loading,
  url: state.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

