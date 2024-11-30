(ns csele.signatures-test
  (:require [clojure.test :refer :all]
            [csele.fixtures.sigs :as sigs]
            [csele.fixtures.keys :as keys]
            [csele.signatures :refer :all]))

(deftest sign-test
  (let [private-key (:private keys/correct)
        public-key (:public keys/correct)]
    (testing "Generates base64 encoded SHA256/RSA signature"
      (is (= (sign "foo" private-key) sigs/foo-correct-signature)))
    (testing "Signature can be verified"
      (is (verify (sign "foo" private-key) "foo" public-key)))))

(deftest verify-test
  (let [public-key (:public keys/correct)]
    (testing "Can verify SHA256/RSA signature"
      (is (verify sigs/foo-correct-signature "foo" public-key))
      (is (not (verify sigs/foo-correct-signature "hoge" public-key))))))
