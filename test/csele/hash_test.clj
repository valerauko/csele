(ns csele.hash-test
  (:require [clojure.test :refer :all]
            [csele.fixtures.hash :as fix]
            [csele.hash :refer :all])
  (:import [java.io ByteArrayInputStream]))

(deftest sha3-test
  (testing "Generates 512 bit SHA3 hash by default"
    (is (= (hash-hex "foo") fix/foo-sha3-512)))
  (testing "Can generate other valid SHA3 bit strengths"
    (is (= (hash-hex "foo" 384) fix/foo-sha3-384))))

(deftest base64-test
  (let [consumed-input (doto (-> "foo" .getBytes ByteArrayInputStream.)
                         (.readAllBytes))] ;; "consume" the stream, throw away
    (testing "Produces correct base64 hash of input"
      (is (= (hash-base64 "foo") fix/foo-base64)))
    (testing "Works with consumed streams too"
      (is (= (hash-base64 consumed-input) fix/foo-base64)))))
