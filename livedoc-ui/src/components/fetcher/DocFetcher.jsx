// @flow
import React from 'react';
import { InlineForm } from '../forms/InlineForm';

type Props = {
  fetchDoc: (url: string) => void,
}

export const DocFetcher = (props: Props) => (<InlineForm hintText='URL to JSON documentation' btnLabel='Get Doc'
                                                         initialValue={computeInitialUrl()}
                                                         onSubmit={props.fetchDoc}/>);

function computeInitialUrl(): string {
  const url = new URL(window.location.href);
  const specifiedUrl = url.searchParams.get('url');
  if (specifiedUrl) {
    return specifiedUrl;
  }
  return window.location.href + 'jsondoc';
}
