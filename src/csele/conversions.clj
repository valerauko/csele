(ns csele.conversions
  (:import [java.io
            InputStream]))

(defprotocol Byteish
  (->bytes ^bytes [_]))

(extend-protocol Byteish
  (Class/forName "[B")
  (->bytes [input] input)

  String
  (->bytes [input] (.getBytes input))

  InputStream
  (->bytes [input] (.readAllBytes input)))
