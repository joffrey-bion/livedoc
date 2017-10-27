// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import type { LivedocType, TypeElement } from '../../../../model/livedoc';

export type TypeInfoProps = {
  type: LivedocType,
  required: ?boolean,
}

type TypeElementProps = {
  element: TypeElement,
}

const TypeElementLink = ({element}: TypeElementProps) => {
  if (element.link) {
    return <Link to={'/types/' + element.link}>{element.text}</Link>
  } else {
    return element.text;
  }
};

export const TypeInfo = (props: TypeInfoProps) => {
  const mark = computeMark(props.required);
  const elements = props.type.typeElements.map(e => <TypeElementLink key={e.text} element={e}/>);

  // this 'if' statement handles old versions of Livedoc
  if (props.type.typeElements) {
    return <code>{elements}{mark}</code>;
  } else {
    // TODO remove this branch when it becomes unnecessary
    return <code>{props.type.oneLineText + mark}</code>;
  }
};

function computeMark(required: ?boolean): string {
  switch (required) {
    case true:
      return '*';
    case false:
      return '?';
    default:
      return '';
  }
}

