(ns chroma-clj.tenant
  (:require [chroma-clj.config :refer [config]]
            [chroma-clj.utils :as util]))

(defn tenant
  "Returns an existing tenant by name."
  [tenant-name]
  (util/make-chroma-request
   (assoc @config :tenant tenant-name)
   :get "tenants/{tenant}" {}))
