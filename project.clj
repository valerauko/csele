(defproject social.kitsune/csele "0.5.0"
  :description "A library to handle ActivityPub-related crypto needs."
  :url "https://kitsune.social"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :dependencies [[commons-codec/commons-codec "1.13"]
                 [org.bouncycastle/bcprov-jdk15on "1.64"]
                 [org.bouncycastle/bcpkix-jdk15on "1.64"]
                 [byte-streams "0.2.4"]]
  :deploy-repositories {"clojars" {:url "https://repo.clojars.org/"}
                        :username :env/clojars_user
                        :password :env/clojars_token}
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]]
                   :plugins [[lein-ancient "0.6.15"
                              :exclusions [org.clojure/clojure]]]}
             :clj1.9.0 {:dependencies
                        [[org.clojure/clojure "1.9.0"]]}
             :clj1.10.0 {:dependencies
                         [[org.clojure/clojure "1.10.0"]]}
             :clj1.10.1 {:dependencies
                         [[org.clojure/clojure "1.10.1"]]}
             :clj1.10.2 {:dependencies
                         [[org.clojure/clojure "1.10.2"]]}
             :clj1.10.3 {:dependencies
                         [[org.clojure/clojure "1.10.3"]]}
             :clj1.11.1 {:dependencies
                         [[org.clojure/clojure "1.11.1"]]}})
