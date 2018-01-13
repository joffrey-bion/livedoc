// @flow
import * as React from 'react';
import { Card } from 'reactstrap';

export type MonospacedSinkCardProps = {
  content: ?string,
}

export const MonospacedSinkCard = ({content}: MonospacedSinkCardProps) => {
  if (!content) {
    return null;
  }
  return <Card style={{padding: '0.5rem'}}>
    <pre>
    {content}
    </pre>
  </Card>;
};
