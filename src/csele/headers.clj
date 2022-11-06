(ns csele.headers
  (:require [csele.signatures :as sig]
            [csele.hash :refer [hash-base64]]
            [clojure.string :refer [split join]]))

(defn sig-string
  "Generates the signature string based on the given Ring request map and
   the list of headers to use. The 'special headers' `(request-target)`,
   `digest` and `host` are changed as the spec demands. All other headers are
   used as-is. Ref: https://tools.ietf.org/html/draft-cavage-http-signatures-06"
  [{method :request-method
    path :uri
    headers :headers
    body :body}
   targets]
  (join "\n"
    (map
      (fn [header]
        (str (name header) ": "
             (case header
               "(request-target)" (str (name method) " " path)
               "digest" (str "SHA-256=" (hash-base64 body))
               ; remove port from host header if present
               "host" (->> header (get headers) (re-find #"[^:]+"))
               (get headers header))))
      targets)))

(defn verify
  "Verifies the signature of a request."
  [{{sig-header "signature"} :headers
    {{actor :actor} :body} :parameters
    :as request}
   input-key]
  (try
    (let [target-headers (split (->> sig-header
                                     (re-find #"headers=\"([^\"]+)\"")
                                     second)
                                #"\s+")
          signature (->> sig-header
                         (re-find #"signature=\"([^\"]+)\"")
                         second)
          computed-string (sig-string request target-headers)]
      (sig/verify signature computed-string input-key))
    (catch Exception e
      ; there are a bunch of reasons this might fail:
      ; * missing / incorrectly formatted signature header
      ; * borked key
      ; * whatever the jvm may come up with
      nil)))

(defn sign-request
  "Signature string for a Ring request map with the given private key."
  [request targets {:keys [key-id pem]}]
  (let [new-string (sig-string request targets)
        signature (sig/sign new-string pem)]
    (format "keyId=\"%s\",algorithm=\"%s\",headers=\"%s\",signature=\"%s\""
            key-id "rsa-sha256" (join " " targets) signature)))
