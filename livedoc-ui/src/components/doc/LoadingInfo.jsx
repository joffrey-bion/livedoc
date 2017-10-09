// @flow
import * as React from 'react';
import { ProgressBar } from 'react-bootstrap';

export type LoadingInfoProps = {
  url: string
}

export const LoadingInfo = (props: LoadingInfoProps) => {
  return <ProgressBar active striped max={100} now={100} label={'Loading documentation from ' + props.url + '...'}/>;
};

