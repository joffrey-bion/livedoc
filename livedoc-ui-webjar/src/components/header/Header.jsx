// @flow
import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Navbar, NavbarBrand } from 'reactstrap';
import type { State } from '../../model/state';
import { getLoadedDocUrl, isDocLoaded } from '../../redux/doc';
import { actions } from '../../redux/actions/loader';
import './Header.css';
import logo from './livedoc-logo-round-white.svg';
import { TopNav } from './TopNav';

export type HeaderProps = {
  uiVersion: string,
  homeUrl: string,
  docLoaded: boolean,
  reset: () => void,
}

const HeaderLogo = () => (<img src={logo} alt="Livedoc Logo" width={20} className="logo"/>);

const CloseButton = ({reset}) => (<button type="button" onClick={reset} className="close" aria-label="Reset">
  <span aria-hidden="true">&times;</span>
</button>);

const HeaderPresenter = ({uiVersion, homeUrl, docLoaded, reset}: HeaderProps) => (<Navbar className="header">
  <NavbarBrand href={homeUrl}>
    <HeaderLogo/>
    <span className="title">Livedoc UI</span>
    <span className="version">v{uiVersion}</span>
  </NavbarBrand>
  {docLoaded && <TopNav/>}
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
  reset: actions.reset,
};

export const Header =  withRouter(connect(mapStateToProps, mapDispatchToProps)(HeaderPresenter));
