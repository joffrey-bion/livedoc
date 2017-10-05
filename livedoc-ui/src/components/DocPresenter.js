// @flow
import {List, Map} from 'immutable';
import * as React from 'react';
import {connect} from 'react-redux';
import type {Livedoc} from '../models/livedoc';
import type {State} from '../models/state';
import type {Api} from '../models/api';

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
  const apis: Map<string, List<Api>> = props.livedoc.apis;
  const nbApis = apis.size;
  return <div>
    <p>nbApisGroups = {nbApis || "undef"}</p>
    <p>apis.get? = {apis.get !== undefined}</p>
  </div>
};

const mapStateToProps = (state: State) => ({
  loading: state.loading,
  url: state.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

