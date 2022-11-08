(ns csele.signatures
  (:require [csele.conversions :refer [->bytes]]
            [csele.keys :as keys])
  (:import [java.security Security Signature]
           [java.io ByteArrayInputStream]
           [java.util Base64]
           [org.bouncycastle.jce.provider BouncyCastleProvider
                                          JCERSAPublicKey]))

(Security/addProvider (BouncyCastleProvider.))

(def algo "SHA256withRSA")

(defn verify
  "Expects a base64 encoded signature"
  [^String signature actual-data public-key]
  (let [sig (doto (Signature/getInstance algo)
                  (.initVerify ^JCERSAPublicKey
                    (keys/string-to-key public-key))
                  (.update (->bytes actual-data)))]
    (.verify sig
      (.decode (Base64/getDecoder) signature))))

(defn sign
  "Produces a base64 encoded signature"
  [data private-key]
  (let [sig (doto (Signature/getInstance algo)
                  (.initSign (keys/string-to-key private-key))
                  (.update (->bytes data)))]
    (.encodeToString (Base64/getEncoder)
      (.sign sig))))
