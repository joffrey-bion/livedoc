// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import {Livedoc} from '../models/livedoc';
import {State} from '../models/state';

type Props = {
  loading: boolean,
  url: ?string,
  livedoc: ?Livedoc
}

const DocPresenter = (props: Props) => {
  if (props.loading) {
    return <span>Loading documentation from {props.url}</span>
  }
  if (!props.livedoc) {
    return <span>Please provide a URL to fetch a documentation.</span>
  }
  const nbApis = props.livedoc.apis.size;
  return <div>There are {nbApis} groups of APIs</div>
};

const mapStateToProps = (state: State) => ({
  loading: state.loading,
  url: state.url,
  livedoc: state.livedoc
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

