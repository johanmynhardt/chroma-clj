(ns chroma-clj.collections
  (:require [chroma-clj.config :only [config]]
            [chroma-clj.utils :as util]))


(defn create-collection
  "Create or get a collection with the given `name`, `metadata`, and `configuration`."
  [name & {:keys [metadata configuration] :or {metadata {} configuration {}}}]
  (util/make-chroma-request @config :post "collections"
                            {:body {:name           name
                                    :get_or_create  true
                                    :metadata       metadata
                                    :configuration  configuration}}))


(defn get-collections
  "List collections with optional pagination parameters: `offset` and `limit`."
  [& {:keys [offset limit] :or {offset 0 limit 100}}]
  (util/make-chroma-request @config :get "collections"
                            {:params {:offset offset :limit limit}}))


(defn get-collection
  "Retrieve a collection by `name`."
  [name]
  (util/make-chroma-request @config :get (str "collections/" name) {}))


(defn update-collection
  "Update a collection's `name` and/or `metadata`."
  [collection-id & {:keys [name metadata]}]
  (when (not (or name metadata))
    (throw (ex-info "Either `name` or `metadata` must be provided." {})))
  (util/make-chroma-request @config :put (str "collections/" collection-id)
                            {:body (cond-> {}
                                     name     (assoc :new_name name)
                                     metadata (assoc :new_metadata metadata))}))


(defn delete-collection
  "Delete a collection by `collection-id`."
  [collection-id]
  (util/make-chroma-request @config :delete (str "collections/" collection-id) {}))
