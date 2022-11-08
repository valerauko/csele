(ns csele.hash
  (:require [csele.conversions :refer [->bytes]])
  (:import [java.io InputStream]
           [java.util Base64]
           [java.security MessageDigest]
           [org.apache.commons.codec.binary Hex]
           [org.bouncycastle.jcajce.provider.digest SHA3$DigestSHA3]))

(defn hash-hex
  "SHA3 hash of string"
  ([input] (hash-hex input 512))
  ([input ^Integer strength]
   (let [bytes (->bytes input)
         sha3 (SHA3$DigestSHA3. strength)]
     (.update sha3 bytes)
     (Hex/encodeHexString (.digest sha3)))))

(defn hash-base64
  "Base64 encoded SHA-256 hash of input."
  [input]
  (if (instance? InputStream input) (.reset ^InputStream input))
  (let [bytes (->bytes input)
        bowel (MessageDigest/getInstance "SHA-256")
        encoder (Base64/getEncoder)]
    (->> bytes
         (.digest bowel)
         (.encodeToString encoder))))
