// @flow
import * as React from 'react';
import {Col, Grid, Row} from 'react-bootstrap';
import {connect} from 'react-redux';
import {Livedoc} from '../../model/livedoc';
import type {State} from '../../model/state';
import DocContent from './content/DocContent';
import {GlobalInfo} from './GlobalInfo';
import {LoadingInfo} from './LoadingInfo';
import NavPanel from './nav/NavPanel';

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

  return <section className='App-content'>
    <Grid fluid>
      <Row>
        <Col md={3}>
          <GlobalInfo livedoc={props.livedoc}/>
          <NavPanel/>
        </Col>
        <Col md={6}>
          <DocContent/>
        </Col>
      </Row>
    </Grid>
  </section>;
};

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  url: state.loader.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(DocPresenter);

