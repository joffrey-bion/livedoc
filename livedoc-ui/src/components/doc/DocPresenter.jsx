// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { Livedoc } from '../../model/livedoc';
import type { State } from '../../model/state';
import { GlobalInfo } from './GlobalInfo';
import NavPanel from './nav/NavPanel';
import { LoadingInfo } from './LoadingInfo';

type Props = {
  loading: boolean, url: ?string, livedoc: ?Livedoc
}

const DocPresenter = (props: Props) => {
  if (props.loading) {
    return <LoadingInfo url={props.url}/>;
  }
  if (!props.livedoc) {
    return <p>Please provide a URL to fetch a documentation.</p>;
  }

  return <div className='App-content'>
    <GlobalInfo livedoc={props.livedoc}/>
    <NavPanel />
  </div>;
};

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  url: state.loader.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

