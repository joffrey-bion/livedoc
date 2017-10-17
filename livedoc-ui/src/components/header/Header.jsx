// @flow
import React from 'react';
import { Button, Navbar, NavbarBrand } from 'reactstrap';
import { connect } from 'react-redux';
import { actions } from '../../redux/reducer';
import type { State } from '../../model/state';
import './Header.css';
import logo from './livedoc-logo-round-white.svg';

export type HeaderProps = {
  homeUrl: string,
  docLoaded: boolean,
  reset: () => void,
}

const HeaderLogo = () => (<img src={logo} alt="Livedoc Logo" width={20} className="logo"/>);

const Header = ({homeUrl, docLoaded, reset}: HeaderProps) => (<Navbar className="header">
  <NavbarBrand href={homeUrl} className="title"><HeaderLogo/>Livedoc</NavbarBrand>
  {docLoaded && <Button color="info" onClick={reset}>Reset</Button>}
</Navbar>);

const getHomeUrl = (currentDocUrl: ?string) => {
  return currentDocUrl ? '/?url=' + currentDocUrl : '/';
};

const mapStateToProps = (state: State) => ({
  homeUrl: getHomeUrl(state.loader.url),
  docLoaded: !!state.livedoc,
});

const mapDispatchToProps = {
  reset: actions.reset,
};

export default connect(mapStateToProps, mapDispatchToProps)(Header);
