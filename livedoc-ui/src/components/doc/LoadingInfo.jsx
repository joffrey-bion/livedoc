// @flow
import * as React from 'react';
import { ProgressBar } from 'react-bootstrap';

type Props = {
  url: string
}

export const LoadingInfo = (props: Props) => {
  return <ProgressBar active striped max={100} now={100} label={'Loading documentation from ' + props.url + '...'}/>;
};

