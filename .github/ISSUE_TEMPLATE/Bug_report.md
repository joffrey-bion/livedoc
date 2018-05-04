---
name: Bug report
about: Create a report to help us improve

---

**Describe the bug**
A clear and concise description of what the bug is.
If relevant (and if not confidential), please include:
- the steps to reproduce the issue
- the error message or stack trace
- any piece of your Java code that could cause the issue
- the JSON output of your Livedoc endpoint (as an attached JSON file or URL)

**Expected behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Environment/Context**
Please provide the following pieces of information if you can, as it helps a lot narrowing down the potential root causes of the problem:
- Livedoc version
  - the UI version appears in the top left-hand corner next to the logo
  - the backend version appears in the JSON of your `/jsondoc` endpoint
- Livedoc flavour (Spring Boot, Spring MVC, plain annotations...)
- Spring version (if using a Spring flavour of Livedoc)
- Your custom Jackson `ObjectMapper` configuration, if using one

Please add any other piece of information you find important to mention about the environment.
