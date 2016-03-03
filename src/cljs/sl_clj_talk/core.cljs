(ns sl-clj-talk.core
  (:require
   [reveal]
   cljsjs.highlight
   cljsjs.highlight.langs.clojure-repl
   cljsjs.highlight.langs.clojure
   cljsjs.highlight.langs.javascript
   cljsjs.highlight.langs.ruby
   [reagent.core :as reagent ;; :refer [atom]
    ]
   [sl-clj-talk.slides :refer [slides-div]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:reveal-state nil})) ;; normal atom

(def slides
  (reagent/create-class
   {:reagent-render slides-div
    :component-did-mount
    (fn [this]
      (let [reveal-state (:reveal-state @app-state)]
        (doto js/Reveal
          (.initialize (clj->js {:history true}))
          (cond->
              reveal-state  (.setState reveal-state)))
        ))}))


(defonce watch-state
  (.addEventListener js/Reveal "slidechanged"
                     (fn [e]
                       (swap! app-state assoc :reveal-state (.getState js/Reveal)))))

(reagent/render-component [slides]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
