// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { Livedoc } from '../../model/livedoc';
import type { State } from '../../model/state';
import { GlobalInfo } from './GlobalInfo';
import { NavPanel } from './nav/NavPanel';

type Props = {
  loading: boolean, url: ?string, livedoc: ?Livedoc
}

const DocPresenter = (props: Props) => {
  if (props.loading) {
    return <span>Loading documentation from {props.url}</span>;
  }
  if (!props.livedoc) {
    return <span>Please provide a URL to fetch a documentation.</span>;
  }

  return <div className='App-content'>
    <GlobalInfo livedoc={props.livedoc}/>
    <NavPanel livedoc={props.livedoc} onSelect={id => console.log('Element selected with id =', id)}/>
  </div>;
};

const mapStateToProps = (state: State) => ({
  loading: state.loading, url: state.url, livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

