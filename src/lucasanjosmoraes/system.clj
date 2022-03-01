(ns lucasanjosmoraes.system
  (:require [com.stuartsierra.component :as component]
            [lucasanjosmoraes.schema :as schema]
            [lucasanjosmoraes.server :as server]
            [lucasanjosmoraes.db :as db]))

(defn new-system
  []
  (merge (component/system-map)
    (server/new-server)
    (schema/new-schema-provider)
    (db/new-db)))
