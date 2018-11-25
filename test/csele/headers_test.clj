(ns csele.headers-test
  (:require [clojure.test :refer :all]
            [csele.fixtures.headers :as headers]
            [csele.fixtures.keys :as keys]
            [csele.headers :refer :all]))

(deftest sign-test
  (let [targets ["(request-target)" "host" "date" "digest" "content-type"]
        private-key {:key-id "hoge"
                     :pem (:private keys/correct)}]
    (testing "Generates correct HTTP header signature RSA-SHA256"
      (is (= headers/dummy-signature (sign-request headers/dummy-request
                                                   targets
                                                   private-key))))))

(deftest verify-test
  (let [public-key (:public keys/correct)]
    (testing "Can verify request signature"
      (is (verify headers/signed-request public-key))
      (is (not (verify headers/signed-request (:public keys/wrong)))))))
