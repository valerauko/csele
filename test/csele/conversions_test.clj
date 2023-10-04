(ns csele.conversions-test
  (:require [clojure.test :refer :all]
            [csele.conversions :refer [->bytes]])
  (:import [java.io
            ByteArrayInputStream]
           [java.util
            Arrays]))

(deftest bytes-test
  (testing "'Converting' bytes"
    (let [byte-arr (.getBytes "foo")]
      (is (= (->bytes byte-arr) byte-arr))))
  (testing "Converting a string"
    (let [string "Lorem ipsum"]
      (is (Arrays/equals (->bytes string) (.getBytes string)))))
  (when-not (= (System/getProperty "java.version") "8")
    (testing "Converting a stream"
      (let [stream (-> "foo" .getBytes ByteArrayInputStream.)]
        (is (Arrays/equals (->bytes stream) (.getBytes "foo")))
        (is (Arrays/equals (->bytes stream) (.getBytes "foo"))
            "Can be called repeatedly (on a resetable stream)"))))
  (testing "Converting null"
    (is (thrown? IllegalArgumentException (->bytes nil))
        "Not supported")))
