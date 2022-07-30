# hh-user-service

CSV pulling pipeline for user registration eligibility

Prerequisites (MacOs):
1) install Homebrew: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
2) install sbt: `brew install sbt`
4) install mysql: `brew install mysql` or dockerized (requires docker) `docker run --name mysql -d \
   -p 3306:3306 \
   -e MYSQL_ROOT_PASSWORD= \
   --restart unless-stopped \
   mysql:8`
5) in application.conf, db connection is configured with (username=root, password=""). Change it if needed.
6) run `mysql`
7) run `mysql> CREATE DATABASE IF NOT EXISTS hh;`

Instructions (run from terminal):
1) under project root dir, run the application: `sbt 'run 9082' -Dplay.server.http.idleTimeout=infinite` (assuming port 9082 is free)
2) initialize application db tables by running `curl http://localhost:9082/init`
3) run scenario:
   1) pull client1 file from 2022-07-26: `curl http://localhost:9082/pull/1/20220726`
   2) check eligibility status for user identified by (clientId = 1, employeeId = 2): `curl http://localhost:9082/check?cid=1&eid=2`. it should return true.
   3) pull client1 file from 2022-07-27: `curl http://localhost:9082/pull/1/20220727`
   4) check (clientId = 1, employeeId = 2) again. it should now return false, as this user is removed in the latest snapshot.
4) pulling large files:
   1) pull client1 file from 2022-07-28, it has 1 million rows. `curl http://localhost:9082/pull/1/20220728`
   * inserting to the db is performed asynchronously in batches in parallel. batch size is configurable in application.conf, currently set at 500.
5) tests:
   1) run with `sbt test`
   * tests currently consists of:
     * full flow tests with scenarios for client2 using in memory db (h2)
     * unit tests for parsers

Some explanations are in order :)
1) files puller is represented by the PullerApp for convenience although it should probably be a simple scala
   app (main) that is triggered by a cronjob with concurrency policy = 1 to ensure continuous polling of new files
2) I use yyyyMMdd pattern working under the assumption that we can identify a file by its creation timestamp 
   or tag it by the cronjob trigger timestamp.
   