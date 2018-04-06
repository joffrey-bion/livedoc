# Change Log

## [v4.3.0](https://bintray.com/joffrey-bion/maven/livedoc/4.3.0) (2018-04-06)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v4.2.0...v4.3.0)

**Implemented enhancements:**

- Properly handle authentication [\#82](https://github.com/joffrey-bion/livedoc/issues/82)

**Closed issues:**

- Livedoc-UI forgets the url if an error occurs during loading [\#80](https://github.com/joffrey-bion/livedoc/issues/80)
- Remove deprecated stuff from Gradle build [\#77](https://github.com/joffrey-bion/livedoc/issues/77)

## [v4.2.0](https://bintray.com/joffrey-bion/maven/livedoc/4.2.0) (2018-03-29)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v4.1.0...v4.2.0)

**Implemented enhancements:**

- Add support for reading documentation from the Javadoc [\#70](https://github.com/joffrey-bion/livedoc/issues/70)

## [v4.1.0](https://bintray.com/joffrey-bion/maven/livedoc/4.1.0) (2018-03-03)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v4.0.0...v4.1.0)

**Implemented enhancements:**

- Remove @ApiVisibility feature [\#74](https://github.com/joffrey-bion/livedoc/issues/74)
- Add support for other Spring mapping annotations [\#69](https://github.com/joffrey-bion/livedoc/issues/69)

**Closed issues:**

- livedoc-ui-webjar v4 missing from MavenCentral [\#72](https://github.com/joffrey-bion/livedoc/issues/72)

## [v4.0.0](https://bintray.com/joffrey-bion/maven/livedoc/4.0.0) (2018-03-02)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v3.0.0...v4.0.0)

**Implemented enhancements:**

- Make types clickable and lead to type doc [\#62](https://github.com/joffrey-bion/livedoc/issues/62)
- Keep doc after refresh using redux local storage [\#60](https://github.com/joffrey-bion/livedoc/issues/60)
- Add 1 or 2 element\(s\) in examples of arrays [\#54](https://github.com/joffrey-bion/livedoc/issues/54)
- Negated spring header support [\#37](https://github.com/joffrey-bion/livedoc/issues/37)
- Create a maintainable UI [\#28](https://github.com/joffrey-bion/livedoc/issues/28)

**Fixed bugs:**

- @PathVariable-annotated param name is empty [\#71](https://github.com/joffrey-bion/livedoc/issues/71)
- @ApiObjectProperty is not allowed on getters [\#53](https://github.com/joffrey-bion/livedoc/issues/53)

**Closed issues:**

- Add proper redirect when no doc is loaded [\#66](https://github.com/joffrey-bion/livedoc/issues/66)

**Merged pull requests:**

- Ensure the div in a NavGroup also has a key [\#68](https://github.com/joffrey-bion/livedoc/pull/68) ([mebubo](https://github.com/mebubo))
- Correctly extract value in onChange [\#67](https://github.com/joffrey-bion/livedoc/pull/67) ([mebubo](https://github.com/mebubo))

## [v3.0.0](https://bintray.com/joffrey-bion/maven/livedoc/3.0.0) (2017-10-04)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v2.1.1...v3.0.0)

**Implemented enhancements:**

- Add Javadoc on all public classes [\#47](https://github.com/joffrey-bion/livedoc/issues/47)
- Add default value for java.util.Date in templates [\#45](https://github.com/joffrey-bion/livedoc/issues/45)
- Add custom default values support in templates [\#44](https://github.com/joffrey-bion/livedoc/issues/44)
- Provide way to set or determine basePath at runtime [\#12](https://github.com/joffrey-bion/livedoc/issues/12)

**Fixed bugs:**

- ObjectMapper custom configuration is ignored by Livedoc [\#43](https://github.com/joffrey-bion/livedoc/issues/43)
- Jsondoc examples does not use settings from springs ObjectMapper [\#42](https://github.com/joffrey-bion/livedoc/issues/42)
- NoClassDefFoundError: org/springframework/messaging/MessageHeaders [\#41](https://github.com/joffrey-bion/livedoc/issues/41)
- Remove hard dependency on spring-boot-starter-web in livedoc-springmvc [\#40](https://github.com/joffrey-bion/livedoc/issues/40)

**Closed issues:**

- Make the version of Livedoc a global variable in the doc [\#51](https://github.com/joffrey-bion/livedoc/issues/51)
- Use tabs for dependency inclusion with Gradle/Maven [\#50](https://github.com/joffrey-bion/livedoc/issues/50)
- Add quick-start documentation in the README [\#48](https://github.com/joffrey-bion/livedoc/issues/48)

## [v2.1.1](https://bintray.com/joffrey-bion/maven/livedoc/2.1.1) (2017-09-28)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v2.1.0...v2.1.1)

**Implemented enhancements:**

- Support cross-origin requests on the jsondoc endpoint [\#35](https://github.com/joffrey-bion/livedoc/issues/35)

**Fixed bugs:**

- NoClassDefFoundError: org/springframework/messaging/handler/annotation/MessageMapping [\#36](https://github.com/joffrey-bion/livedoc/issues/36)
- Type-level path not taken into account for inherited methods [\#34](https://github.com/joffrey-bion/livedoc/issues/34)

## [v2.1.0](https://bintray.com/joffrey-bion/maven/livedoc/2.1.0) (2017-09-26)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v2.0.0...v2.1.0)

**Implemented enhancements:**

- Move annotations to own jar/lib [\#33](https://github.com/joffrey-bion/livedoc/issues/33)

## [v2.0.0](https://bintray.com/joffrey-bion/maven/livedoc/2.0.0) (2017-09-24)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v1.2.0...v2.0.0)

**Fixed bugs:**

- Complicated enum templates [\#32](https://github.com/joffrey-bion/livedoc/issues/32)
- Empty templates in the UI [\#31](https://github.com/joffrey-bion/livedoc/issues/31)
- Broken playground [\#29](https://github.com/joffrey-bion/livedoc/issues/29)

**Merged pull requests:**

- Fix broken handlebars due to formatting [\#30](https://github.com/joffrey-bion/livedoc/pull/30) ([joffrey-bion](https://github.com/joffrey-bion))

## [v1.2.0](https://bintray.com/joffrey-bion/maven/livedoc/1.2.0) (2017-09-13)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v1.1.0...v1.2.0)

**Implemented enhancements:**

- Use custom properties exploration for templates generation [\#20](https://github.com/joffrey-bion/livedoc/issues/20)

## [v1.1.0](https://bintray.com/joffrey-bion/maven/livedoc/1.1.0) (2017-09-10)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v1.0.2...v1.1.0)

**Implemented enhancements:**

- Refactor types management [\#25](https://github.com/joffrey-bion/livedoc/issues/25)
- Scan Controllers' inherited methods [\#6](https://github.com/joffrey-bion/livedoc/issues/6)

**Fixed bugs:**

- Default method verbs not set properly [\#27](https://github.com/joffrey-bion/livedoc/issues/27)

## [v1.0.2](https://bintray.com/joffrey-bion/maven/livedoc/1.0.2) (2017-09-04)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v1.0.1...v1.0.2)

**Fixed bugs:**

- Broken types display [\#24](https://github.com/joffrey-bion/livedoc/issues/24)

## [v1.0.1](https://bintray.com/joffrey-bion/maven/livedoc/1.0.1) (2017-09-04)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v1.0.0...v1.0.1)

**Fixed bugs:**

- Private non-annotated methods appear in the doc [\#23](https://github.com/joffrey-bion/livedoc/issues/23)

## [v1.0.0](https://bintray.com/joffrey-bion/maven/livedoc/1.0.0) (2017-09-01)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.4.5...v1.0.0)

**Implemented enhancements:**

- Make Livedoc more customizable by decoupling components [\#21](https://github.com/joffrey-bion/livedoc/issues/21)
- Improve objects docs by using actually serialized properties [\#19](https://github.com/joffrey-bion/livedoc/issues/19)

## [v0.4.5](https://bintray.com/joffrey-bion/maven/livedoc/0.4.5) (2017-08-27)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.4.4...v0.4.5)

**Fixed bugs:**

- Broken type info in livedoc UI [\#18](https://github.com/joffrey-bion/livedoc/issues/18)

## [v0.4.4](https://bintray.com/joffrey-bion/maven/livedoc/0.4.4) (2017-08-26)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.4.0...v0.4.4)

**Fixed bugs:**

- Livedoc UI webjar is empty [\#17](https://github.com/joffrey-bion/livedoc/issues/17)

## [v0.4.0](https://bintray.com/joffrey-bion/maven/livedoc/0.4.0) (2017-08-23)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.3.0...v0.4.0)

**Implemented enhancements:**

- Ignore fields annotated @JsonIgnore [\#10](https://github.com/joffrey-bion/livedoc/issues/10)

**Closed issues:**

- Customize Spring MVC types exploration using Jackson properties [\#15](https://github.com/joffrey-bion/livedoc/issues/15)
- Add proper support for custom property exploration [\#14](https://github.com/joffrey-bion/livedoc/issues/14)

## [v0.3.0](https://bintray.com/joffrey-bion/maven/livedoc/0.3.0) (2017-08-15)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.2.0...v0.3.0)

**Implemented enhancements:**

- Support MessageMapping implicit Spring payload parameter [\#11](https://github.com/joffrey-bion/livedoc/issues/11)

**Fixed bugs:**

- Broken generic types exploration [\#13](https://github.com/joffrey-bion/livedoc/issues/13)
- RequestParam\#name\(\) ignored [\#9](https://github.com/joffrey-bion/livedoc/issues/9)
- Incorrect path joining between controller and methods [\#7](https://github.com/joffrey-bion/livedoc/issues/7)

## [v0.2.0](https://bintray.com/joffrey-bion/maven/livedoc/0.2.0) (2017-08-13)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.1.1...v0.2.0)

**Implemented enhancements:**

- Type names shouldn't be forced to lowercase [\#5](https://github.com/joffrey-bion/livedoc/issues/5)
- Allow custom scanner in JsonDocController [\#2](https://github.com/joffrey-bion/livedoc/issues/2)

**Fixed bugs:**

- Livedoc ignores transient modifier \(leading to StackOverflowError\) [\#4](https://github.com/joffrey-bion/livedoc/issues/4)

## [v0.1.1](https://bintray.com/joffrey-bion/maven/livedoc/0.1.1) (2017-08-13)
[Full Changelog](https://github.com/joffrey-bion/livedoc/compare/v0.1.0...v0.1.1)

**Fixed bugs:**

- Typo in package name for livedoc-springboot [\#1](https://github.com/joffrey-bion/livedoc/issues/1)



\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*