// @flow
import * as React from 'react';
import { MonospacedSinkCard } from './MonospacedSinkCard';

export type JsonCardProps = {
  jsonObject: ?any,
}

export const JsonCard = ({jsonObject}: JsonCardProps) => {
  if (!jsonObject) {
    return null;
  }
  return <MonospacedSinkCard content={JSON.stringify(jsonObject, null, 2)}/>;
};
