(ns chroma-clj.core-test
  (:require [clojure.test :refer :all]
            [chroma-clj.collections :as c]
            [chroma-clj.core :refer :all]
            [chroma-clj.config :refer [config]]
            [chroma-clj.tenant :as t]
            [chroma-clj.utils :as util]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 0))))

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
  (pprint (c/get-collections))
  (pprint (c/get-collection "collection"))
  (pre-flight-checks)
  (t/tenant "default")
  ;(t/databases)
  (c/get-collections)
  (c/collections-count)
  (c/record-count (:id (first (c/get-collections))))
  )
