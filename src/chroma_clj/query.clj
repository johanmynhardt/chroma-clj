(ns chroma-clj.query
  (:require [chroma-clj.config :only [config]]
            [chroma-clj.utils :as util]))


(defn query
  "Perform a KNN search based on `query-embedding` and optional filters."
  [collection-id query-embedding
   & {:keys [where where-document include num-results]
      :or {include [:metadatas :documents :distances]
           num-results 10}}]
  (let [path (str "collections/" collection-id "/query")
        body {:query_embeddings [query-embedding]
              :n_results        num-results
              :include          include}
        body (cond-> body
               where           (assoc :where where)
               where-document  (assoc :where_document where-document))]
    (util/make-chroma-request @config :post path {:body body})))


(defn query-batch
  "Perform a batch KNN search with multiple `query-embeddings`."
  [collection-id query-embeddings
   & {:keys [where where-document include num-results]
      :or {include [:metadatas :documents :distances]
           num-results 10}}]
  (let [path (str "collections/" collection-id "/query")
        body {:query_embeddings query-embeddings
              :n_results        num-results
              :include          include}
        body (cond-> body
               where           (assoc :where where)
               where-document  (assoc :where_document where-document))]
    (util/make-chroma-request @config :post path {:body body})))
