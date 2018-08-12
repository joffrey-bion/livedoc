// @flow
import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Navbar, NavbarBrand } from 'reactstrap';
import type { State } from '../../model/state';
import { actions } from '../../redux/actions/loader';
import { getLoadedDocUrl, isDocLoaded } from '../../redux/doc';
import { IconButton } from '../shared/button/IconButton';
import './Header.css';
import logo from './livedoc-logo-round-white.svg';
import { TopNav } from './TopNav';

export type HeaderProps = {
  uiVersion: string,
  homeUrl: string,
  docLoaded: boolean,
  saveDoc: () => void,
  reset: () => void,
}

const HeaderLogo = () => (<img src={logo} alt="Livedoc Logo" width={20} className="logo"/>);

const ToolbarIconButton = ({icon, onClick, ...props}) => (
        <IconButton icon={icon} onClick={onClick} className="toolbar-icon" {...props} />
);

const CloseButton = ({reset}) => <ToolbarIconButton title="Reset" icon="times" onClick={reset} />;

const SaveButton = ({saveDoc}) => <ToolbarIconButton title="Save doc" icon="download" onClick={saveDoc} />;

const HeaderPresenter = ({uiVersion, homeUrl, docLoaded, saveDoc, reset}: HeaderProps) => (<Navbar className="header">
  <NavbarBrand href={homeUrl}>
    <HeaderLogo/>
    <span className="title">Livedoc UI</span>
    <span className="version">v{uiVersion}</span>
  </NavbarBrand>
  {docLoaded && <TopNav/>}
  {docLoaded && <SaveButton saveDoc={saveDoc}/>}
  {docLoaded && <CloseButton reset={reset}/>}
</Navbar>);

const getHomeUrl = (currentDocUrl: ?string) => {
  return currentDocUrl ? '/?url=' + currentDocUrl : '/';
};

const mapStateToProps = (state: State) => ({
  uiVersion: state.uiVersion,
  homeUrl: getHomeUrl(getLoadedDocUrl(state)),
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {
  saveDoc: actions.saveDoc,
  reset: actions.reset,
};

export const Header =  withRouter(connect(mapStateToProps, mapDispatchToProps)(HeaderPresenter));
