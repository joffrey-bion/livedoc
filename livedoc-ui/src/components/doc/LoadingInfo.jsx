// @flow
import * as React from 'react';
import { Progress } from 'reactstrap';

export type LoadingInfoProps = {
  url: string
}

export const LoadingInfo = (props: LoadingInfoProps) => {
  return <Progress animated value={100} style={{padding: '5px'}}>Loading documentation from {props.url}...</Progress>;
};

