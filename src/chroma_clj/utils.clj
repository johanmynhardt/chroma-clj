(ns chroma-clj.utils
  "Core API functions for interacting with ChromaDB."
  (:require [cheshire.core :as json]
            [org.httpkit.client :as http]
            [selmer.parser :as s]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]])
  (:refer-clojure :exclude [get update count]))

(def ^:dynamic *debug* false)

(defn url-hydrate-params
  "Hydrates the path parameters using selmer.

  Eg.: :path of `tenants/{tenant}/databases/{database}` with
       :m of {:tenant \"foo\" :database \"blurb\"} will result
       in string of \"tenants/foo/databases/blurb\"."
  [m path]
  (-> path
      (str/replace "{" "{{")
      (str/replace "}" "}}")
      (s/render m)))

(defn generate-url
  "Construct the full URL for a given API `path` using the current `config`."
  [config path & [at-root?]]
  (str (:uri config) (if at-root? "/" "/api/v2/") (url-hydrate-params config path)))


(defn- handle-response
  "Process the HTTP `response`, throwing an exception if the status is not successful."
  [response]
  (let [{:keys [error status body]} (deref response)
        return
        (cond error
              (throw error)

              (<= 200 status 299)
              (json/parse-string body keyword)

              (<= status 499)
              (let [parsed-body (json/parse-string body keyword)
                    {:keys [error message]} parsed-body]
                (throw (ex-info (format "%s: %s" error message)
                                {:status status
                                 :body parsed-body})))

              :else
              (throw (ex-info
                      "HTTP ServerError"
                      {:status status
                       :body (try
                               (json/parse-string body keyword)
                               (catch Exception _ body))})))]
    (when *debug*
      (pprint {:chroma/response return}))
    return))


(defn make-chroma-request
  "Make an HTTP request with the given parameters and process the response."
  [config method path {:keys [params body at-root?]}]
  (let [{:keys [timeout api-key tenant database]} config
        headers (cond-> {"Content-Type" "application/json"}
                  api-key (assoc "x-chroma-token" api-key))
        query-params (merge params {:tenant tenant :database database})
        url (generate-url config path at-root?)
        opts {:method          method
              :url             url
              :headers         headers
              :query-params    query-params
              :body            (when body (json/generate-string body))
              :socket-timeout  timeout
              :conn-timeout    timeout
             ; :as              :json-string-keys
              :throw-exceptions false}]
    (when *debug* (pprint {:chroma/request (update-in opts [:body] (fn [_] body))}))
    (handle-response (http/request opts))))
