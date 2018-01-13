// @flow
import * as React from 'react';
import { Col, Container, Row } from 'reactstrap';
import NavPanel from '../../shared/nav/NavPanel';
import { GeneralInfo } from '../../shared/general/GeneralInfo';
import { GlobalDocPanel } from './GlobalDocPanel';

export const GlobalDocScene = () => {
  return <Container fluid>
    <Row>
      <Col md={3}>
        <GeneralInfo/>
        <NavPanel/>
      </Col>
      <Col md={9}>
        <GlobalDocPanel/>
      </Col>
    </Row>
  </Container>;
};


