// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Col, Container, Row } from 'reactstrap';
import type { Livedoc } from '../../model/livedoc';
import type { State } from '../../model/state';
import { ContentPanel } from './content/ContentPanel';
import { GlobalInfo } from './GlobalInfo';
import { LoadingInfo } from './LoadingInfo';
import NavPanel from './nav/NavPanel';
import { SidePanel } from './SidePanel';

export type DocPresenterProps = {
  loading: boolean,
  url: ?string,
  livedoc: ?Livedoc
}

const DocPresenter = (props: DocPresenterProps) => {
  if (props.loading && props.url) {
    return <LoadingInfo url={props.url}/>;
  }
  if (!props.livedoc) {
    return <p>Please provide a URL to fetch a documentation.</p>;
  }

  return <section className='App-content'>
    <Container fluid>
      <Row>
        <Col md={3}>
          <GlobalInfo livedoc={props.livedoc}/>
          <NavPanel/>
        </Col>
        <Col md={6}>
          <ContentPanel/>
        </Col>
        <Col md={3}>
          <SidePanel/>
        </Col>
      </Row>
    </Container>
  </section>;
};

const mapStateToProps = (state: State) => ({
  loading: state.loader.loading,
  url: state.loader.url,
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(DocPresenter));

