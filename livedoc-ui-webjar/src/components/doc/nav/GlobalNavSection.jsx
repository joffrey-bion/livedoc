// @flow
import * as React from 'react';
import type { ApiGlobalDoc } from '../../../model/livedoc';
import type { NavElementDescription, NavGroupDescription } from './NavGroup';
import { NavSection } from './NavSection';

export type GlobalNavSectionProps = {
  globalDoc: ApiGlobalDoc,
}

export const GlobalNavSection = ({globalDoc, ...otherProps}: GlobalNavSectionProps) => {
  const hasGeneral = globalDoc.sections.length > 0;
  const hasChangeLogs = globalDoc.changelogset.changelogs.length > 0;
  const hasMigrations = globalDoc.migrationset.migrations.length > 0;

  const globalElements: NavElementDescription[] = [];
  globalElements.push(navElementDesc('general', 'General', hasGeneral));
  globalElements.push(navElementDesc('changelog', 'Change Log', hasChangeLogs));
  globalElements.push(navElementDesc('migrations', 'Migrations', hasMigrations));

  const globalGroup: NavGroupDescription = {elements: globalElements};

  return <NavSection groups={[globalGroup]} {...otherProps}/>;
};

function navElementDesc(link: string, name: string, enabled: boolean): NavElementDescription {
  return {link, name, disabled: !enabled};
}
