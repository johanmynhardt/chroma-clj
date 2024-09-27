(ns chroma-clj.core
  "Core API functions for interacting with ChromaDB."
  (:gen-class)
  (:require [chroma-clj.config :refer [config]]
            [chroma-clj.utils :as util]))


(defn get-version
  "Retrieve the ChromaDB server version."
  []
  (util/make-chroma-request @config :get "version" {}))


(defn health-check
  "Check if the ChromaDB server is active and responding."
  []
  (util/make-chroma-request @config :get "heartbeat" {}))

