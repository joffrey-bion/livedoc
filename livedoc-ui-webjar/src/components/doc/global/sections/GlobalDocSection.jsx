// @flow
import * as React from 'react';
import type { ApiGlobalSectionDoc } from '../../../../model/livedoc';

export type GlobalDocSectionProps = {
  section: ApiGlobalSectionDoc,
}

export const GlobalDocSection = (props: GlobalDocSectionProps) => {

  const paragraphs = props.section.paragraphs.map((content, index) => <p key={index}>{content}</p>);

  return <section>
    <h2>{props.section.title}</h2>
    {paragraphs}
  </section>;
};
