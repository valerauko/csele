(ns csele.keys
  (:import [java.security KeyFactory KeyPair KeyPairGenerator]
           [java.security.interfaces RSAPublicKey]
           [java.security.spec X509EncodedKeySpec PKCS8EncodedKeySpec]
           [java.io StringReader StringWriter]
           ; REVIEW: maybe it could be done without depending on bc?
           [org.bouncycastle.openssl PEMParser PEMWriter PEMKeyPair]
           [org.bouncycastle.asn1.x509 SubjectPublicKeyInfo]
           [org.bouncycastle.crypto.util PrivateKeyFactory]
           [java.util Base64]))

(defn raw-keys
  "Generates raw keys with the given strength."
  [^Integer strength]
  (let [generator (doto (KeyPairGenerator/getInstance "RSA")
                        (.initialize strength))]
    (.generateKeyPair generator)))

(defn string-to-key
  "Parses a PEM string into a (public or private) key, whichever it is."
  [input]
  (let [input-key (-> input StringReader. PEMParser. .readObject)]
    ; private key: PEMKeyPair
    ; public key: SubjectPublicKeyInfo
    (let [key-factory (KeyFactory/getInstance "RSA")]
      (if (instance? PEMKeyPair input-key)
        (let [bytes (-> ^PEMKeyPair input-key .getPrivateKeyInfo .getEncoded)
              key-spec (PKCS8EncodedKeySpec. bytes)]
          (.generatePrivate key-factory key-spec))
        (let [bytes (.getEncoded ^SubjectPublicKeyInfo input-key)
              key-spec (X509EncodedKeySpec. bytes)]
          (.generatePublic key-factory key-spec))))))

(defn key-to-string
  "Turns a (public or private) key into a string."
  [input-key]
  (let [string-writer (StringWriter.)
        pem-writer (PEMWriter. string-writer)]
    (.writeObject pem-writer input-key)
    (.flush pem-writer)
    (.toString string-writer)))

(defn generate-keypair
  "Generates a keypair and returns it in a hashmap as PEM strings.
   You can set the strength (length) of the key generated. Defaults to 1024."
  ([] (generate-keypair 1024))
  ([strength]
   (let [^KeyPair keys (raw-keys strength)]
     {:public (-> keys .getPublic key-to-string)
      :private (-> keys .getPrivate key-to-string)})))

(defn salmon-public-key
  "Outputs a public key in the magic-public-key format used for Webfinger in
   current federated networks such as Mastodon and Pleroma."
  ; TODO: link spec in docstring if there's one at all
  [^String input]
  (let [^RSAPublicKey input-key (string-to-key input)
        modulus (-> input-key .getModulus .toByteArray)
        exponent (-> input-key .getPublicExponent .toByteArray)
        encoder (Base64/getUrlEncoder)]
    (str "RSA."
         (.encodeToString encoder modulus)
         "."
         (.encodeToString encoder exponent))))
