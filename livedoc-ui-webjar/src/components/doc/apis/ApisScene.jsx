// @flow
import * as React from 'react';
import { Col, Container, Row } from 'reactstrap';
import type { LivedocID } from '../../../model/livedoc';
import NavPanel from '../../shared/nav/NavPanel';
import Playground from '../../playground/Playground';
import { GeneralInfo } from '../../shared/general/GeneralInfo';
import { ApiDocPanel } from './ApiDocPanel';

export type ApisSceneProps = {
  selectedApiId: ?LivedocID,
  selectedMethodId: ?LivedocID,
}

export const ApisScene = ({selectedApiId, selectedMethodId}: ApisSceneProps) => {
  return <Container fluid>
    <Row>
      <Col md={3}>
        <GeneralInfo />
        <NavPanel/>
      </Col>
      <Col md={5}>
        <ApiDocPanel apiId={selectedApiId} selectedMethodId={selectedMethodId}/>
      </Col>
      <Col md={4}>
        <Playground selectedApiId={selectedApiId} selectedMethodId={selectedMethodId}/>
      </Col>
    </Row>
  </Container>;
};


