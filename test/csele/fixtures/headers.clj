(ns csele.fixtures.headers)

(def dummy-request
  {:uri "/"
   :request-method :post
   :headers {"content-type" "application/activity+json"
             "date" "Sun, 25 Nov 2018 08:08:46 GMT"
             "host" "example.com"}
   :body "foo"})

(def dummy-signature
  "keyId=\"hoge\",algorithm=\"rsa-sha256\",headers=\"(request-target) host date digest content-type\",signature=\"AhXPP5lUQJgcF6Om3A0K/UCnJ5AWYc+zvSZFuVhNHVHGVKSiFf3IDeKDv0i2agOLY/6LjxmbQBZ9Raqjq9sFFrhWe7mRuut5U0TdWYoElaY45iuXyMIlwERqHppLOvWRI+MubWlZCyMKKRxfxbJ+mCXXXNTKxg+5Fvc3ugvrD5I=\""                             )

(def signed-request
  (assoc-in dummy-request [:headers :signature] dummy-signature))
