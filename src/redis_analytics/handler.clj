(ns redis-analytics.handler
  (:require [redis-analytics.storage :as storage]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :refer (response redirect content-type)]
            [clojure.pprint :refer (pprint)]))

(defn echo
  "Echos the request back as a string."
  [request]
  (-> (response (with-out-str (pprint request)))
    (content-type "text/plain")))

(defn handle-hello
  [request]
  (storage/log-request! request)
  (str "{\"message\": \"hello world\"}"))

(defn handle-logs
  [request]
  "{\"logset\": [\"endpoint\": \"hello-world\", \"logs\": [{\"ip\":
      \"xxx.xxx.xxx.xxx\", \"timestamp\": xxxxxxxxxxx}]]}")

(defn handle-hello-logs
  [request]
  "{\"logs\": [{\"ip\": \"xxx.xxx.xxx.xxx”, \"timestamp\": xxxxxxxxxxx}]}")

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/v1/logs" request (handle-logs request))
  (GET "/v1/hello-world" request (handle-hello request))
  (GET "/v1/hello-world/logs" request (handle-hello-logs request))
  (ANY "*" [] echo)
  (route/not-found "{\"error\": \"Not Found\"}"))

(def app
  (wrap-defaults app-routes (assoc api-defaults :proxy false)))
