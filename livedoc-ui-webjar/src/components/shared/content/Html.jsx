//@flow
import * as React from 'react';
import activeHtml from 'react-active-html';
import { LivedocLink } from './LivedocLink';

const componentsMap = {
  a: props => <Anchor {...props} />,
};

export type HtmlProps = {
  content?: string,
}

export const Html = ({content}: HtmlProps) => {
  if (content == null || content.length === 0) {
    return null;
  }
  return activeHtml(content, componentsMap);
};

const Anchor = (props) => {
  if (props.href.indexOf('livedoc://') === 0) {
    return <LivedocLink url={props.href} {...props}/>;
  }
  const {href, children, ...other} = props;
  return <a href={href} {...other}>{children}</a>;
};
