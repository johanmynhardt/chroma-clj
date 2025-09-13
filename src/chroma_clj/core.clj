(ns chroma-clj.core
  "Core API functions for interacting with ChromaDB."
  (:gen-class)
  (:require [chroma-clj.config :refer [config]]
            [chroma-clj.utils :as util]))

(defn openapi []
  (util/make-chroma-request @config :get "openapi.json" {:at-root? true}))

(defn auth-identity
  "Retrieves the current user's identity, tenant, and databases."
  []
  (util/make-chroma-request @config :get "auth/identity" {}))

(defn get-version
  "Returns the version of the server."
  []
  (util/make-chroma-request @config :get "version" {}))

(defn heartbeat
  "Heartbeat endpoint that returns a nanosecond timestamp of the current time."
  []
  (util/make-chroma-request @config :get "heartbeat" {}))

(defn healthcheck
  "Health check endpoint that returns 200 if the server and executor are ready"
  []
  (util/make-chroma-request @config :get "healthcheck" {}))

(defn pre-flight-checks
  "Pre-flight checks endpoint reporting basic readiness info."
  []
  (util/make-chroma-request @config :get "pre-flight-checks" {}))
