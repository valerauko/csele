(ns csele.signatures
  (:require [csele.keys :as keys]
            [byte-streams :as bs])
  (:import [java.security Security Signature]
           [java.io ByteArrayInputStream]
           [java.util Base64]
           [org.bouncycastle.jce.providee BouncyCastleProvider]))

(Security/addProvider (BouncyCastleProvider.))

(def algo "SHA256withRSA")

(defn verify
  "Expects a base64 encoded signature"
  [^String signature actual-data public-key]
  (let [sig (doto (Signature/getInstance algo)
                  (.initVerify ^org.bouncycastle.jce.provider.JCERSAPublicKey
                    (keys/string-to-key public-key))
                  (.update (bs/to-byte-array actual-data)))]
    (.verify sig
      (.decode (Base64/getDecoder) signature))))

(defn sign
  "Produces a base64 encoded signature"
  [data private-key]
  (let [sig (doto (Signature/getInstance algo)
                  (.initSign (keys/string-to-key private-key))
                  (.update (bs/to-byte-array data)))]
    (.encodeToString (Base64/getEncoder)
      (.sign sig))))
