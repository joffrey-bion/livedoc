// @flow
import * as React from 'react';
import type { ApiGlobalDoc, GlobalDocPage } from '../../../model/livedoc';
import type { NavElementDescription, NavGroupDescription } from './NavGroup';
import { NavSection } from './NavSection';

export type GlobalNavSectionProps = {
  globalDoc: ApiGlobalDoc,
  match: any,
  location: any,
}

export const GlobalNavSection = ({globalDoc, ...otherProps}: GlobalNavSectionProps) => {

  const globalGroup: NavGroupDescription = {
    elements: globalDoc.pages.map(navElementDesc)
  };
  return <NavSection groups={[globalGroup]} {...otherProps}/>;
};

function navElementDesc(page: GlobalDocPage): NavElementDescription {
  return {link: page.livedocId, name: page.title, disabled: false};
}
