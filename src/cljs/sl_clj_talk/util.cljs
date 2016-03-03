(ns sl-clj-talk.util
  (:require
   [reagent.core :as reagent :refer [atom]]
   [clojure.string :as cstring]))

(defn highlight! [component]
  (.highlightBlock js/hljs
                   (reagent/dom-node component)))

(defn wrap-highlight [component]
  (reagent/create-class
   {:reagent-render component
    :component-did-mount highlight!
    :component-did-update highlight!}))

(defn code-block* [code-type code-str]
  [:code
   {:class (str "hljs "
                code-type)}
   code-str])


(def code-block
  (wrap-highlight
   code-block*))


(defn js-example [indent-n & ss]
  [:pre
   [code-block
    "javascript"
    (str
     ;; "\n"
     (cstring/join "\n"
                  (map (partial str
                                (apply str
                                       (repeat indent-n " ")))
                       ;; handle existing line breaks
                       (flatten (map cstring/split-lines ss))))
     "\n" ;; "\n\n"
     )]])

(defn clj-example [indent-n & ss]
  [:pre
   [code-block
    "clojure"
    (str
     ;; "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        (flatten (map cstring/split-lines ss))))
     "\n" ;; "\n\n"
     )]])

(defn clj-repl-example [indent-n & ss]
  [:pre
   [code-block
    "clojure"
    (str
     ;; "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        (flatten (map cstring/split-lines ss))))
     ;; "\n\n"
     "\n")]])

(defn clj-data-example [indent-n & data]
  [:pre
   [code-block
    "clojure"
    (str
     "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        data))
     "\n\n")]])


(defn fragment-list [tag & items]
  (into
   [tag]
   (for [item items]
     [:li.fragment item])))
