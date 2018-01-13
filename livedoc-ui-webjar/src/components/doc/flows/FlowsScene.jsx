// @flow
import * as React from 'react';
import { Col, Container, Row } from 'reactstrap';
import type { LivedocID } from '../../../model/livedoc';
import NavPanel from '../../shared/nav/NavPanel';
import { GeneralInfo } from '../../shared/general/GeneralInfo';
import { FlowDocPanel } from './FlowDocPanel';

export type FlowsSceneProps = {
  selectedFlowId: ?LivedocID,
  selectedMethodId: ?LivedocID,
}

export const FlowsScene = ({selectedFlowId, selectedMethodId}: FlowsSceneProps) => {
  return <Container fluid>
    <Row>
      <Col md={3}>
        <GeneralInfo />
        <NavPanel/>
      </Col>
      <Col md={9}>
        <FlowDocPanel flowId={selectedFlowId} selectedMethodId={selectedMethodId}/>
      </Col>
    </Row>
  </Container>;
};


