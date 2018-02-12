// @flow
import * as React from 'react';
import type { ApiGlobalSectionDoc } from '../../../../model/livedoc';

export type GlobalDocSectionProps = {
  section: ApiGlobalSectionDoc,
}

const HtmlParagraph = ({content}) => <p dangerouslySetInnerHTML={{__html: content}}/>;

export const GlobalDocSection = (props: GlobalDocSectionProps) => {

  const paragraphs = props.section.paragraphs.map((content, index) => <HtmlParagraph key={index} content={content}/>);

  return <section>
    <h2>{props.section.title}</h2>
    {paragraphs}
  </section>;
};
