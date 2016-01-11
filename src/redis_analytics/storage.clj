(ns redis-analytics.storage
  (:require [taoensso.carmine :as r :refer (wcar)]))

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}}) ; See `wcar` docstring for opts
(defmacro redis-client [& body] `(r/wcar redis-conn ~@body))

(defn redis-safe-ip
  "Convert IPv6 address 0:...:1 to Redis key-safe 0-...-1"
  [ip-address]
  (clojure.string/replace ip-address #":" "-"))

(defn request-ip
  "Extract IP Address from HTTP request"
  [request]
  (redis-safe-ip
   ; get IP even if behind reverse-proxy
   (or (get-in request [:headers "x-forwarded-for"]) (:remote-addr request))))

(defn current-timestamp
  []
  (quot (System/currentTimeMillis) 1000))

(defn log-request!
  [request]
  (redis-client
   ; create per-second key (if not exists)
   (r/setnx (str "user-request:"
                 (request-ip request) ":"
                 (current-timestamp)) 0)
   ; increment for current request
   (r/incr (str "user-request:"
                 (request-ip request) ":"
                 (current-timestamp)))))

(defn example
  []
  (redis-client
   (r/ping)
   (r/set "foo" "bar")
   (r/get "foo")))

