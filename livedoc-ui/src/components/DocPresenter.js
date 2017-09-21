// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import {Livedoc} from '../models/livedoc';

type Props = {
  livedoc: ?Livedoc
}

const DocPresenter = (props: Props) => {
  if (!props.livedoc) {
    return <span>Please provide a URL to fetch a documentation.</span>
  }
  const nbApis = props.livedoc.apis.size;
  return <div>There are {nbApis} groups of APIs</div>
};

const mapStateToProps = state => ({
  livedoc: state.get('livedoc')
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

