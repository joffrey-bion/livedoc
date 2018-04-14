// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import type { LivedocType, TypeElement } from '../../../model/livedoc';
import { RouteHelper } from '../../../routing/routeHelper';

export type TypeRefProps = {
  type: LivedocType,
  required?: string,
}

type TypeElementProps = {
  element: TypeElement,
}

const TypeElementLink = ({element}: TypeElementProps) => {
  if (element.livedocId) {
    return <Link to={RouteHelper.typeUrl(element.livedocId)}>{element.text}</Link>;
  } else {
    return element.text;
  }
};

export const TypeRef = (props: TypeRefProps) => {
  const mark = computeMark(props.required);
  const elements = props.type.typeElements.map((e, index) => <TypeElementLink key={index} element={e}/>);
  return <code>{elements}{mark}</code>;
};

function computeMark(required: ?string): string {
  switch (required) {
    case 'true':
      return '*';
    case 'false':
      return '?';
    default:
      return '';
  }
}

