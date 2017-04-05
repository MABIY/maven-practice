# mvn 实战
   ---
   
   mvn 更新模块
   ```shell
   mvn versions:set -DnewVersion=2.50.1-SNAPSHOT
   ```
   It will adjust all pom versions, parent versions and dependency versions in a multi-module project.
   
   If you made a mistake, do
   ```shell
   mvn versions:revert
   ```
   afterwards, or
   ```shell
   mvn versions:commit
   ```
1
1
12
