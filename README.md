# JWraith

[![Build Status](https://travis-ci.org/earelin/JWraith.svg)](https://travis-ci.org/earelin/JWraith)
[![codecov](https://codecov.io/gh/earelin/JWraith/branch/master/graph/badge.svg)](https://codecov.io/gh/earelin/JWraith)

A Java clone of the Wraith website comparison tool.

## Usage

```
jwraith [browsers|capture|history|latest|spider] [configuration file path]
```

Modes of operation:

browsers: Compares a website among different browsers  
capture: Compares between two websites  
history: Creates a history capture to be compared later  
latest: Compares current website with the last history capture  
spider: Crawls website to get all paths  

### Use cases

#### Compare stating and production websites

Configuration file

```yaml
browser_name: "phantomjs"
driver_executable: "/usr/local/bin/phantomjs"

directory: "shots"

domains:
  base: "http://example.com"
  compare: "http://staging.example.com"
  
paths_file: "paths.txt"

workers: 4

screen_widths:
  - 320
  - 1280
  
spider_skips:
  - "^/ajax/.*"
  - "^/calendar/ical/.*"

reports:
  threshold: 15
  thumbnail_width: 300
  thumbnail_height: 300
```

First we must capture all website paths:

```
jwraith spider configuration.yml
```

Once we have the links we can launch the compare process

```
jwraith compare configuration.yml
```
