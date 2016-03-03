(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [org.httpkit.server :as s]))


;; Binding/Destructuring

(let [some-scalar 100
      [a b c :as some-vec] [1 2 3]
      {:keys [foo bar baz] :as some-map} {:foo 1 :bar 2 :baz 3}]
  (and
   (= some-scalar 100)
   (= some-vec [1 2 3])
   (= some-map {:foo 1 :bar 2 :baz 3})
   (= a foo 1)
   (= b bar 2)
   (= c baz 3)))


;; Collections can be efficiently combined with into

(into {}
      [[:foo "bar"]
       [:baz "quxx"]])

;; list comprehension

(def people
  [{:id 1 :username "Jason"}
   {:id 2 :username "Milt"}
   {:id 3 :username "Rich Hickey"}])

(for [{:keys [id username]} people
      :when (= id 3)]
  username)

;; macro club
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was unsuccessful:" result#))))


;; Web!

(defonce server
  (atom nil))

(defroutes app
  (GET "/" [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn start-server! []
  (reset! server (s/run-server app {:port 9000})))

(defn stop-server! []
  (when-let [s @server]
    (s)
    (reset! server nil)))















(def lines
  (atom []))

(defn read-lines [data path & [drop-n take-n]]
  (with-open [rdr (io/reader path)]
    (doseq [line (cond->> (rest (line-seq rdr))
                   drop-n (drop drop-n)
                   take-n (take take-n))]
      (swap! data conj line))))
