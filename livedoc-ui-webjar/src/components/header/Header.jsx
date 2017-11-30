// @flow
import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { Navbar, NavbarBrand } from 'reactstrap';
import type { State } from '../../model/state';
import { isDocLoaded } from '../../redux/livedoc';
import { actions } from '../../redux/actions/loader';
import './Header.css';
import logo from './livedoc-logo-round-white.svg';
import { TopNav } from './TopNav';

export type HeaderProps = {
  homeUrl: string,
  docLoaded: boolean,
  reset: () => void,
}

const HeaderLogo = () => (<img src={logo} alt="Livedoc Logo" width={20} className="logo"/>);

const CloseButton = ({reset}) => (<button type="button" onClick={reset} className="close" aria-label="Reset">
  <span aria-hidden="true">&times;</span>
</button>);

const Header = ({homeUrl, docLoaded, reset}: HeaderProps) => (<Navbar className="header">
  <NavbarBrand href={homeUrl} className="title"><HeaderLogo/>Livedoc</NavbarBrand>
  {docLoaded && <TopNav/>}
  {docLoaded && <CloseButton reset={reset}/>}
</Navbar>);

const getHomeUrl = (currentDocUrl: ?string) => {
  return currentDocUrl ? '/?url=' + currentDocUrl : '/';
};

const mapStateToProps = (state: State) => ({
  homeUrl: getHomeUrl(state.loader.url),
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {
  reset: actions.reset,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Header));
