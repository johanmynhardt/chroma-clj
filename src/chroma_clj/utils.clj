(ns chroma-clj.utils
  "Core API functions for interacting with ChromaDB."
  (:require [cheshire.core :as json]
            [org.httpkit.client :as http])
  (:refer-clojure :exclude [get update count]))


(defn generate-url
  "Construct the full URL for a given API `path` using the current `config`."
  [config path]
  (str (:uri config) "/api/v2/" path))


(defn- handle-response
  "Process the HTTP `response`, throwing an exception if the status is not successful."
  [response]
  (let [{:keys [error status body]} (deref response)]
    (cond error
          (throw error)

          (<= 200 status 299)
          (json/parse-string body keyword)

          :else
          (throw (ex-info
                  "HTTP Error"
                  {:status status
                   :body (try
                           (json/parse-string body true)
                           (catch Exception _ body))})))))


(defn make-chroma-request
  "Make an HTTP request with the given parameters and process the response."
  [config method path {:keys [params body]}]
  (let [{:keys [timeout api-key tenant database]} config
        headers (cond-> {"Content-Type" "application/json"}
                  api-key (assoc "x-chroma-token" api-key))
        query-params (merge params {:tenant tenant :database database})
        url (generate-url config path)
        opts {:method          method
              :url             url
              :headers         headers
              :query-params    query-params
              :body            (when body (json/generate-string body))
              :socket-timeout  timeout
              :conn-timeout    timeout
             ; :as              :json-string-keys
              :throw-exceptions false}]
    (handle-response (http/request opts))))
