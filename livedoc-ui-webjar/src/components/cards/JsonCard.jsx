// @flow
import 'highlight.js/styles/magula.css';
import './JsonCard.css';
import * as React from 'react';
import Highlight from 'react-syntax-highlight';
import { Card } from 'reactstrap';

export type JsonCardProps = {
  jsonObject: ?any,
}

export const JsonCard = ({jsonObject}: JsonCardProps) => {
  if (!jsonObject) {
    return null;
  }
  return <Card>
    <Highlight lang='json' value={JSON.stringify(jsonObject, null, 2)} style={{marginBottom: 0}}/>
  </Card>;
};
