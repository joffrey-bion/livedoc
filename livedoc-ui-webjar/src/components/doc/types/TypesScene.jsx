// @flow
import * as React from 'react';
import { Col, Container, Row } from 'reactstrap';
import type { LivedocID } from '../../../model/livedoc';
import NavPanel from '../../shared/nav/NavPanel';
import { GeneralInfo } from '../../shared/general/GeneralInfo';
import { TypeDocPanel } from './TypeDocPanel';
import { TypeExample } from './TypeExample';

export type TypesSceneProps = {
  selectedTypeId: ?LivedocID,
}

export const TypesScene = ({selectedTypeId}: TypesSceneProps) => {
  return <Container fluid>
    <Row>
      <Col md={3}>
        <GeneralInfo />
        <NavPanel/>
      </Col>
      <Col md={5}>
        <TypeDocPanel typeId={selectedTypeId}/>
      </Col>
      <Col md={4}>
        <TypeExample typeId={selectedTypeId}/>
      </Col>
    </Row>
  </Container>;
};


