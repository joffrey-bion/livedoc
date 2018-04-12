// @flow
import * as React from 'react';
import type { ApiGlobalDoc } from '../../../model/livedoc';
import type { NavElementDescription, NavGroupDescription } from './NavGroup';
import { NavSection } from './NavSection';

export type GlobalNavSectionProps = {
  globalDoc: ApiGlobalDoc,
  match: any,
  location: any,
}

export const GlobalNavSection = ({globalDoc, ...otherProps}: GlobalNavSectionProps) => {
  const hasGeneral = globalDoc.general.length > 0;
  // this checks for null AND undefined with "!=" on purpose
  const hasChangeLogs = globalDoc.changelogSet != null && globalDoc.changelogSet.changelogs.length > 0;
  const hasMigrations = globalDoc.migrationSet != null && globalDoc.migrationSet.migrations.length > 0;

  const globalElements: NavElementDescription[] = [];
  globalElements.push(navElementDesc('general', 'General', true));
  globalElements.push(navElementDesc('changelog', 'Change Log', hasChangeLogs));
  globalElements.push(navElementDesc('migrations', 'Migrations', hasMigrations));

  const globalGroup: NavGroupDescription = {elements: globalElements};

  return <NavSection groups={[globalGroup]} {...otherProps}/>;
};

function navElementDesc(link: string, name: string, enabled: boolean): NavElementDescription {
  return {link, name, disabled: !enabled};
}
