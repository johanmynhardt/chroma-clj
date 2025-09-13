(ns chroma-clj.embeddings
  (:require [chroma-clj.config :refer [config]]
            [chroma-clj.utils :as util]))


(defn insert-embeddings
  "Add embeddings to a collection. `embeddings` is a sequence of maps with keys:
   `:id`, `:embedding`, `:metadata`, and `:document`."
  [collection-id embeddings & {:keys [upsert?] :or {upsert? false}}]
  (let [path "tenants/{tenant}/databases/{database}/collections/{collection-id}/{embedding-action}"
        data (let [ids        (mapv :id embeddings)
                   embeddings' (mapv :embedding embeddings)
                   metadatas  (mapv :metadata embeddings)
                   documents  (mapv :document embeddings)]
               (cond-> {}
                 (seq ids)        (assoc :ids ids)
                 (seq embeddings') (assoc :embeddings embeddings')
                 (seq metadatas)  (assoc :metadatas metadatas)
                 (seq documents)  (assoc :documents documents)))]
    (util/make-chroma-request
     (assoc @config
            :collection-id collection-id
            :embedding-action (if upsert? "upsert" "add"))
     :post path {:body data})))


(defn update-embeddings
  "Update embeddings in a collection."
  [collection-id embeddings]
  (let [path "tenants/{tenant}/databases/{database}/collections/{collection-id}/update"
        data (let [ids        (mapv :id embeddings)
                   embeddings' (mapv :embedding embeddings)
                   metadatas  (mapv :metadata embeddings)
                   documents  (mapv :document embeddings)]
               (cond-> {}
                 (seq ids)        (assoc :ids ids)
                 (seq embeddings') (assoc :embeddings embeddings')
                 (seq metadatas)  (assoc :metadatas metadatas)
                 (seq documents)  (assoc :documents documents)))]
    (util/make-chroma-request
     (assoc @config :collection-id collection-id)
     :post path {:body data})))


(defn get-embeddings
  "Retrieve embeddings from a collection with optional filters and pagination."
  [collection-id & {:keys [ids where where-document include offset limit]
                    :or {include [:metadatas :documents]
                         offset 0
                         limit 100}}]
  (let [path "tenants/{tenant}/databases/{database}/collections/{collection-id}/get"
        body (cond-> {}
               ids             (assoc :ids ids)
               where           (assoc :where where)
               where-document  (assoc :where_document where-document)
               include         (assoc :include include))
        params {:offset offset :limit limit}]
    (util/make-chroma-request
     (assoc @config :collection-id collection-id)
     :post path {:params params :body body})))


(defn delete-embeddings
  "Delete embeddings from a collection using `ids`, `where`, or `where-document`."
  [collection-id & {:keys [ids where where-document]}]
  (when-not (or ids where where-document)
    (throw (ex-info "At least one of `ids`, `where`, or `where-document` must be provided." {})))
  (let [path "tenants/{tenant}/databases/{database}/collections/{collection-id}/delete"
        body (cond-> {}
               ids             (assoc :ids ids)
               where           (assoc :where where)
               where-document  (assoc :where_document where-document))]
    (util/make-chroma-request
     (assoc @config :collection-id collection-id)
     :post path {:body body})))
