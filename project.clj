(defproject social.kitsune/csele "0.7.3"
  :description "A library to handle ActivityPub-related crypto needs."
  :url "https://kitsune.social"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :dependencies [[commons-codec/commons-codec "1.22.0"]
                 [org.bouncycastle/bcprov-jdk18on "1.84"]
                 [org.bouncycastle/bcpkix-jdk18on "1.84"]]
  :deploy-repositories {"clojars" {:url "https://repo.clojars.org/"
                                   :username :env/clojars_user
                                   :password :env/clojars_token}}
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.12.5"]
                                  [lambdaisland/kaocha "1.91.1392"]]
                   :global-vars {*warn-on-reflection* true
                                 *unchecked-math* :warn-on-boxed}
                   :plugins [[lein-ancient "0.7.0"
                              :exclusions [org.clojure/clojure]]]}
             :clj1.9 {:dependencies
                      [[org.clojure/clojure "1.9.0"]]}
             :clj1.10 {:dependencies
                       [[org.clojure/clojure "1.10.3"]]}
             :clj1.11 {:dependencies
                       [[org.clojure/clojure "1.11.4"]]}
             :clj1.12 {:dependencies
                       [[org.clojure/clojure "1.12.5"]]}}
  :aliases {"test" ["run" "-m" "kaocha.runner"]})
