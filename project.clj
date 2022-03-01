(defproject lucasanjosmoraes/lacinia-api-poc "0.1.0-SNAPSHOT"
  :description "POC of a GraphQL API using Lacinia"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [com.stuartsierra/component "1.1.0"]
                 [com.walmartlabs/lacinia-pedestal "1.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.3.3"]
                 [com.mchange/c3p0 "0.9.5.5"]
                 [io.aviso/logging "1.0"]]
  :plugins [[lein-ancient "1.0.0-RC3"]])
