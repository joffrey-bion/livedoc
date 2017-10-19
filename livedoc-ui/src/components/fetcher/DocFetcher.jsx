// @flow
import React from 'react';
import { InlineForm } from '../forms/InlineForm';

type Props = {
  fetchDoc: (url: string) => void,
}

export const DocFetcher = (props: Props) => (<InlineForm hintText='URL to JSON documentation' btnLabel='Get Doc'
                                                         initialValue={computeInitialUrl()}
                                                         onSubmit={props.fetchDoc}/>);

const DEFAULT_FETCH_URL = 'http://localhost:8080/jsondoc';

function computeInitialUrl(): string {
  const url = new URL(window.location.href);
  const specifiedUrl = url.searchParams.get('url');
  if (specifiedUrl) {
    return specifiedUrl;
  }
  return DEFAULT_FETCH_URL;
}
