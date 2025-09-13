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

(defn databases
  "Lists all databases for a given tenant."
  []
  (util/make-chroma-request @config :get "tenants/{tenant}/databases" {}))

(defn collections
  "Lists all collections in the specified database."
  []
  (util/make-chroma-request
   @config :get "tenants/{tenant}/databases/{database}/collections" {}))

(defn collections-count
  "Retrieves the total number of collections in a given database."
  []
  (util/make-chroma-request
   @config :get "tenants/{tenant}/databases/{database}/collections_count" {}))

(defn collection
  "Retrieves a collection by ID or name."
  [collection-id-or-name]
  (util/make-chroma-request
   (assoc @config :collection-id-or-name collection-id-or-name)
   :get "tenants/{tenant}/databases/{database}/collections/{collection-id-or-name}" {}))

(defn collection-record-count
  "Retrieves the number of records in a collection."
  [collection-id]
  (util/make-chroma-request
   (assoc @config :collection-id collection-id)
   :get "tenants/{tenant}/databases/{database}/collections/{collection-id}/count" {}))

(defn tenant
  "Returns an existing tenant by name."
  [tenant-name]
  ;tenants/{tenant_name}
  (util/make-chroma-request
   (assoc @config :tenant tenant-name)
   :get "tenants/{tenant}" {}))

(defn pre-flight-checks
  "Pre-flight checks endpoint reporting basic readiness info."
  []
  (util/make-chroma-request @config :get "pre-flight-checks" {}))


(comment
  (require '[clojure.pprint :refer [pprint]])
  (swap! config assoc
         :tenant "default"
         :database "default-database")
  (binding [util/*debug* true]
    (pprint (-> (openapi) :paths keys (sort))))
  (auth-identity)
  (get-version)
  (heartbeat)
  (healthcheck)
  (pprint (collections))
  (pprint (collection "collection"))
  (pre-flight-checks)
  (tenant "default")
  (databases)
  (collections)
  (collections-count)
  (collection-record-count (:id (first (collections))))
  )
