# zone-health

[![Build Status](https://travis-ci.org/hmrc/zone-health.svg?branch=master)](https://travis-ci.org/hmrc/zone-health) [ ![Download](https://api.bintray.com/packages/hmrc/releases/zone-health/images/download.svg) ](https://bintray.com/hmrc/releases/zone-health/_latestVersion)

This microservice has a health check endpoint that will perform the check(s) defined below.
1. Insert into and read from a local mongo instance to ensure that connectivity works
1. Optionally call the health check endpoint on another instance of the service. This can be useful to test the connectivity from one zone to another.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
