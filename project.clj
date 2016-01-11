(defproject redis-analytics "0.1.0-SNAPSHOT"
  :description "Example of API analytics with Redis"
  :url "https://github.com/sventech/redis-analytics"
  :min-lein-version "2.0.0"
  :uberjar-name "redis-analytics.jar"
  :jvm-opts ["-server"]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-core "1.4.0"]
                 [com.taoensso/carmine "2.12.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler redis-analytics.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}
   :uberjar {:omit-source true
             :env {:production true}
             :aot :all}})
