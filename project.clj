(defproject social.kitsune/csele "0.5.0"
  :description "A library to handle ActivityPub-related crypto needs."
  :url "https://kitsune.social"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [commons-codec/commons-codec "1.13"]
                 [org.bouncycastle/bcprov-jdk15on "1.64"]
                 [org.bouncycastle/bcpkix-jdk15on "1.64"]
                 [byte-streams "0.2.4"]]
  :profiles {:dev {:plugins [[lein-ancient "0.6.15"
                              :exclusions [org.clojure/clojure]]]}})
