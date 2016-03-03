(ns sl-clj-talk.repl
  (:require
   [reagent.core :as r]
   [replumb.core :as replumb]
   [replumb.browser :as browser]
   [sl-clj-talk.util :refer [wrap-highlight]]))


(defn eval-string [result-cb user-str]
  (replumb/read-eval-call browser/default-opts
                          result-cb
                          user-str))


(defn repl-code-block* []
  (let [kids (r/children (r/current-component))]
    (into [:code.hljs.clojure] kids)))

(def repl-code-block (wrap-highlight repl-code-block*))

(defn input [input-ra results-ra]
  [:div
   [:span
    "cljs-repl> "]
   [:span
    {:contentEditable true
     :on-input (fn [e] (reset! input-ra (.-textContent  (.-currentTarget e))))
     :on-key-press (fn [e] (let [k (.-key e)]
                            (when (= k "Enter")
                              ;; don't write the enter to the div
                              (.preventDefault e)
                              ;; evaluate the input
                              (eval-string (fn [r] (swap! results-ra conj r))
                                           @input-ra)
                              ;; clear the input
                              (reset! input-ra ""))))
     :value @input-ra}
    ]])

(defn replet []
  (let [user-input (r/atom "")
        results (r/atom [])]
    (fn []
      [:div.replet
       [:pre
        (-> [repl-code-block]
            (into
             (for [{:keys [value error form warning]} @results]

               [:div
                [:div
                 (str "cljs-repl> " form)]
                (when warning
                  (str "WARNING: " warning))
                [:div
                 (str (or value error))]]))
            (conj [input user-input results]))]])))
