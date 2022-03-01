(ns lucasanjosmoraes.db
  (:require
   [com.stuartsierra.component :as component]
   [io.pedestal.log :as log]
   [clojure.java.jdbc :as jdbc]
   [clojure.string :as str])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource)))

(defn ^:private pooled-data-source
  [host dbname user password port]
  {:datasource
   (doto (ComboPooledDataSource.)
     (.setDriverClass "org.postgresql.Driver")
     (.setJdbcUrl (str "jdbc:postgresql://" host ":" port "/" dbname))
     (.setUser user)
     (.setPassword password))})

(defrecord ClojureGameGeekDb [ds]

  component/Lifecycle

  (start [this]
    (assoc this
      :ds (pooled-data-source "localhost" "cggdb" "cgg_role" "lacinia" 25432)))

  (stop [this]
    (-> ds :datasource .close)
    (assoc this :ds nil)))

(defn new-db
  []
  {:db (map->ClojureGameGeekDb {})})

(defn ^:private query
  [component statement]
  (let [[sql & params] statement]
    (log/debug :sql (str/replace sql #"\s+" " ")
      :params params))
  (jdbc/query (:ds component) statement))

(defn ^:private execute!
  [component statement]
  (let [[sql & params] statement]
    (log/debug :sql (str/replace sql #"\s+" " ")
      :params params))
  (jdbc/execute! (:ds component) statement))

(defn find-game-by-id
  [component game-id]
  (first
    (query component
      ["select game_id, name, summary, min_players, max_players
               from board_game where game_id = ?" game-id])))

(defn find-member-by-id
  [component member-id]
  (first
    (query component
      ["select member_id, name
             from member
             where member_id = ?" member-id])))

(defn list-designers-for-game
  [component game-id]
  (query component
    ["select d.designer_id, d.name, d.url
           from designer d
           inner join designer_to_game j on (d.designer_id = j.designer_id)
           where j.game_id = ?
           order by d.name" game-id]))

(defn list-games-for-designer
  [component designer-id]
  (query component
    ["select g.game_id, g.name, g.summary, g.min_players, g.max_players
           from board_game g
           inner join designer_to_game j on (g.game_id = j.game_id)
           where j.designer_id = ?
           order by g.name" designer-id]))

(defn list-ratings-for-game
  [component game-id]
  (query component
    ["select game_id, member_id, rating
           from game_rating
           where game_id = ?" game-id]))

(defn list-ratings-for-member
  [component member-id]
  (query component
    ["select game_id, member_id, rating
           from game_rating
           where member_id = ?" member-id]))

(defn upsert-game-rating
  "Adds a new game rating, or changes the value of an existing game rating.

  Returns nil"
  [component game-id member-id rating]
  (execute! component
    ["insert into game_rating (game_id, member_id, rating)
           values (?, ?, ?)
           on conflict (game_id, member_id) do update set rating = ?"
     game-id member-id rating rating])

  nil)