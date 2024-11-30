(ns csele.keys-test
  (:require [clojure.test :refer :all]
            [csele.fixtures.keys :as fix]
            [csele.keys :refer :all])
  (:import [java.io StringReader]
           [java.security KeyFactory]
           [java.security.spec X509EncodedKeySpec]
           [java.security.interfaces RSAPublicKey RSAPrivateKey]
           [org.bouncycastle.openssl PEMParser PEMKeyPair]))

(deftest conversion-test
  (testing "Key-string conversions are symmetric"
    (let [private-key (string-to-key (:private fix/correct))
          public-key (string-to-key (:public fix/correct))]
      (is (= (key-to-string private-key) (:private fix/correct)))
      (is (= (key-to-string public-key) (:public fix/correct))))))

(deftest generator-test
  (testing "Generates a correct, matching keypair"
    (let [keypair (generate-keypair)
          factory (KeyFactory/getInstance "RSA")
          private (-> (:private keypair)
                      StringReader. PEMParser. .readObject)
          test-public (->> ^PEMKeyPair private .getPublicKeyInfo .getEncoded
                           X509EncodedKeySpec. (.generatePublic factory))]
      (is (instance? RSAPrivateKey (string-to-key (:private keypair))))
      (is (instance? RSAPublicKey (string-to-key (:public keypair))))
      (is (= (key-to-string test-public) (:public keypair))
          "Should be able to generate the public key from the private key"))))

(deftest salmon-test
  (let [public-key (:public fix/correct)]
    (testing "Generates correct magic-public-key from PEM"
      (is (= (salmon-public-key public-key) fix/salmon)))))
