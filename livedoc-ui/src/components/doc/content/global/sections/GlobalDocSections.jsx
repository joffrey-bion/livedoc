// @flow
import * as React from 'react';
import type { ApiGlobalSectionDoc } from '../../../../../model/livedoc';
import { GlobalDocSection } from './GlobalDocSection';
import './GlobalDocSections.css';

export type GlobalDocSectionsProps = {
  sections: ApiGlobalSectionDoc[],
}

export const GlobalDocSections = ({sections}: GlobalDocSectionsProps) => {
  return sections.map(s => <GlobalDocSection key={s.livedocId} section={s}/>);
};
