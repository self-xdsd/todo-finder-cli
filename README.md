## TODO Finder CLI

[![Build Status](https://travis-ci.org/self-xdsd/todo-finder-cli.svg?branch=master)](https://travis-ci.org/self-xdsd/todo-finder-cli)
[![Coverage Status](https://coveralls.io/repos/github/self-xdsd/todo-finder-cli/badge.svg?branch=master)](https://coveralls.io/github/self-xdsd/todo-finder-cli?branch=master)

[![Managed By Self XDSD](https://amihaiemil.com/images/mbself.svg)](#)
[![DevOps By Rultor.com](http://www.rultor.com/b/self-xdsd/todo-finder-cli)](http://www.rultor.com/p/self-xdsd/todo-finder-cli)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)


TODO Finder CLI, version `0.0.4`.

This is a small Java CLI tool which scans the directory where it's being run and reports all the TODOs or FIXMEs found in the code files.

## Contributing 

If you would like to contribute, just open an issue or a PR.

You will need Java 11.
Make sure the maven build:

``$mvn clean install -Pcheckstyle,itcases``

passes before making a PR. [Checkstyle](http://checkstyle.sourceforge.net/) will make sure
you're following our code style and guidelines.

It's better to make changes on a separate branch (derived from ``master``), so you won't have to cherry pick commits in case your PR is rejected.

## LICENSE

This product's code is open source. However, the [LICENSE](https://github.com/self-xdsd/todo-finder-cli/blob/master/LICENSE) only allows you to read the code. Copying, downloading or forking the repo is strictly forbidden unless you are one of the project's contributors.
