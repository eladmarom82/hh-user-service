# hh-user-service

Prerequisites:
1) a local or dockerized mysql running. install by `brew install mysql` or `docker run --name mysql -d \
   -p 3306:3306 \
   -e MYSQL_ROOT_PASSWORD= \
   --restart unless-stopped \
   mysql:8`
2) make sure you use user root with empty password, or change the connection parameters in application.conf
3) run `mysql`
4) run `mysql> CREATE DATABASE IF NOT EXISTS hh`

Instructions (run from terminal):
1) run the application: `sbt 'run 9082' -Dplay.server.http.idleTimeout=infinite` (assuming port 9082 is free)
2) initialize db tables by running `curl http://localhost:9082/init`
3) pull client1 file from 2022-07-27: `curl http://localhost:9082/pull/1/20220727`
4) check current user eligibility status by GET `curl http://localhost:9082/check?cid=1&eid=2`

Some explanations are in order :)
1) files puller is represented by the PullerAppController for convenience although it should probably be a simple scala
   app (main) that is triggered by a cronjob with concurrency policy = 1 to ensure continuous polling of new files
2) I use yyyyMMdd pattern working under the assumption that we can identify a file by its creation timestamp 
   or tag it by the cronjob trigger timestamp.
   