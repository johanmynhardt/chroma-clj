(ns chroma-clj.core
  "Core API functions for interacting with ChromaDB."
  (:gen-class)
  (:require [chroma-clj.config :refer [config]]
            [chroma-clj.utils :as util]))

(defn auth-identity
  []
  (util/make-chroma-request @config :get "auth/identity" {}))

(defn get-version
  "Retrieve the ChromaDB server version."
  []
  (util/make-chroma-request @config :get "version" {}))

(defn heartbeat
  "Check if the ChromaDB server is active and responding."
  []
  (util/make-chroma-request @config :get "heartbeat" {}))

(defn healthcheck
  "Check if the ChromaDB server is active and responding."
  []
  (util/make-chroma-request @config :get "healthcheck" {}))

(defn databases []
  (util/make-chroma-request @config :get "" {}))

(comment

  (auth-identity)
  (get-version)
  (heartbeat)
  (healthcheck)

  (databases)
  )
