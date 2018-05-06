//@flow
import activeHtml from 'react-active-html';

const componentsMap = {
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
