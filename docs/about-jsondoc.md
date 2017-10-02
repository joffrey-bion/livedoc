---
title: About JSONDoc
layout: default
---

# About JSONDoc

[JSONDoc](http://jsondoc.org) is the project that inspired me when I created Livedoc.
I started using it during the development of [Seven Wonders Online](https://github.com/luxons/seven-wonders), but was 
lacking the websocket API support, and facing a few annoying bugs.

## Why not contribute to JSONDoc?

I tried opening issues on the original JSONDoc project, but didn't get any answer there. Then I tried directly 
contacting the author multiple times, to discuss with him improvements I intended to make to support websocket APIs, 
but didn't get any reply either, so I decided to do my own thing.

## Why not a fork?

There are multiple reasons why I created a new project instead of forking JSONDoc:
- I changed almost everything in the code compared to the JSONDoc project, so there was no hope in a future merge
- I changed the build system
- I wanted git tags and versions to be independent form the original JSONDoc project, as I could not upload new JSONDoc 
artifacts myself
- At first, I thought I couldn't open issues on a fork, but it appears we just have to change Github's default setting
