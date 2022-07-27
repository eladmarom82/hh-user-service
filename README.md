# hh-user-service

Prerequisites:
1) a local or dockerized mysql running. install by `brew install mysql` or `docker run --name mysql -d \
   -p 3306:3306 \
   -e MYSQL_ROOT_PASSWORD= \
   --restart unless-stopped \
   mysql:8`
2) run `mysql`
3) run `mysql> CREATE DATABASE IF NOT EXISTS hh`

Instructions:
1) run the application from terminal, run `sbt 'run 9082'` (assuming port 9082 is free)
2) pull client1 file from 2022-07-27 by a GET request `localhost:9082/pull/1/20220727`
3) check current user eligibility status by GET `localhost:9082/check`

Some explanations are in order :)
1) files puller is represented by the PullerAppController for convenience although it should probably be a simple scala
   app (main) that is triggered by a cronjob with concurrency policy = 1 to ensure continuous polling of new files
2) I use yyyyMMdd pattern working under the assumption that we can identify a file by its creation timestamp 
   or tag it by the cronjob trigger timestamp.
   