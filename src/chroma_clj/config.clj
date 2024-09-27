(ns chroma-clj.config
  "Configuration handling for the ChromaDB client."
  (:require [clojure.string :as str]))


(defn get-env
  "Retrieve environment variable `var`, returning `default` if not found."
  ([var] (get-env var nil))
  ([var default]
   (let [val (System/getenv var)]
     (if (and val (not (str/blank? val)))
       val
       default))))


(def default-config
  "Default configuration map for the ChromaDB client."
  {:uri          (get-env "CHROMA_URI" "http://0.0.0.0:8000")
   :api-key      (get-env "CHROMA_API_KEY")
   :timeout      (get-env "CHROMA_TIMEOUT" 10000)
   :tenant       (get-env "CHROMA_TENANT" "default_tenant")
   :database     (get-env "CHROMA_DATABASE" "default_database")
   :allow-reset  (parse-boolean (get-env "CHROMA_ALLOW_RESET" "false"))})


(def config
  "An atom holding the current configuration."
  (atom default-config))


(defn configure
  "Update the client configuration by merging in the provided `opts` map."
  [opts]
  (swap! config merge opts))
