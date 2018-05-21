//@flow
import { CircularProgress } from 'material-ui';
import * as React from 'react';

const absoluteCenter = {
  opacity: 1,
  position: 'absolute',
  margin: 'auto',
  top: 0,
  right: 0,
  bottom: 0,
  left: 0,
};

export const LoadingSpinner = () => {
  return <CircularProgress style={absoluteCenter} size={370} thickness={0.4}/>;
};
