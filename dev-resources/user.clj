(ns user
  (:require
   [com.walmartlabs.lacinia :as lacinia]
   [clojure.java.browse :refer [browse-url]]
   [lucasanjosmoraes.system :as system]
   [lucasanjosmoraes.test-utils :as utils]
   [com.stuartsierra.component :as component]))

(defonce system nil)

(defn q
  [query-string]
  (-> system
    :schema-provider
    :schema
    (lacinia/execute query-string nil nil)
    utils/simplify))

(defn start
  []
  (alter-var-root #'system (fn [_]
                             (-> (system/new-system)
                               component/start-system)))
  (browse-url "http://localhost:8888/ide")
  :started)

(defn stop
  []
  (when (some? system)
    (component/stop-system system)
    (alter-var-root #'system (constantly nil)))
  :stopped)

(comment
  (start)
  (stop)
  )
