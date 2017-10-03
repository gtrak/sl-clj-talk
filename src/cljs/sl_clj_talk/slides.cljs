(ns sl-clj-talk.slides
  (:require
   [sl-clj-talk.repl :refer [replet]]
   [sl-clj-talk.util :refer [wrap-highlight
                             code-block
                             js-example
                             clj-example
                             clj-repl-example
                             clj-data-example
                             fragment-list]]))





(def title-slide
  [:section
   [:h1 "Still 'Getting' Clojure"]
   [:h3 "'(Parentheses are just hugs for your code)"]
   [:small "Contributions spanning years:"
    [:br]
    [:a {:href "http://twitter.com/miltreder"} "Milt Reder"]
    [:br]
    [:a {:href "http://twitter.com/gtrakgt"} "Gary Trakhman"]
    [:br]
    [:a {:href "http://twitter.com/canweriotnow"} "Jason Lewis"]
    [:br]
    [:a {:href "http://twitter.com/meatcomputer"} "Mikaela Patella"]
    [:br]]
   [:br]])

(defn js-2-clojure []
  [:section
   [:div
    [:h3 "Intro: Functions"]
    [js-example
     30
     "function(){"
     "  return 5;"
     "  }"
     ]]
   [:div.fragment
    "Put some parens around it, kill the braces"

    [js-example
     30
     "(function()"
     "  return 5;"
     "  )"
     ]]

   [:div.fragment
    "Change 'function' to 'fn', makes args into a vector"
    [clj-example
     30
     "(fn []"
     "  return 5;"
     "  )"]]

   [:div.fragment
    "Remove 'return', the last value is always returned."
    [clj-example
     30
     "(fn [] 5)"]

    "Welcome to Clojure."]])

(def everything-is-an-expression
  [:section
   [:p
    "Everything is an expression"]
   [:p.fragment
    "Pure functions return values!"]
   [:p.fragment   "It would be pain to write `return` every time."]])

(defn calling-stuff []
  [:section
   [:h3 "Calling Stuff"]
   [js-example
    20
    "someFunction(arg1, arg2, arg3)"]
   [:div.fragment
    "Move the left parenthesis over a bit more..."
    [clj-example
     20
     "(someFunction arg1 arg2 arg3)"]
    "Done."]])


(defn data-intro []
  [:section
   [:h2 "Data"]
   "Should look familiar"
   [clj-data-example
    20
    {:key1 5 :key2 nil}
    [1 2 3 4 "five"]]

   [:p.fragment "Still 'data'"]

   [:div.fragment
    [clj-data-example
     20
     "'(this is a list)"]]
   [:div.fragment
    [clj-data-example
     20
     "'[1 [2] #{3} {4 4} (constantly 5)]"]]

   [:p.fragment "Still 'data', evaluation is transformation"]

   [:div.fragment
    [clj-repl-example
     0
     "=> (range 10)"
     "(0 1 2 3 4 5 6 7 8 9)"
     "=> (take 11 (range))"
     "(0 1 2 3 4 5 6 7 8 9 10)"
     "=> (last (range)) ;; This takes a long time."]]])

(defn everything-is-data []
  [:section
   [:section
    [:h3 "Everything is written as composable Data"]
    [clj-example
     0
     "[:a-keyword"
     " 5"
     " {:a :map}"
     " #{:a :set}"
     " \"a string\""
     " (fn a-function [])"
     " 'a-function]"]

    [:div.fragment "What needs to be in place to make this work?"]
    [:p.fragment
     "Immutability"]
    [:p.fragment
     "Consistent hashing and equality"]]

   [:section
    "We use data to model our domains"
    [:div.fragment
     [clj-example
      0
      ";; ring http request"
      "{:request-method :get"
      " :server-name \"localhost\""
      " :server-port 9000"
      " :scheme :http"
      " :uri \"/foo/bar\""
      " :body #org.httpkit.BytesInputStream[...]"
      " ... }"]]

    [:div.fragment
     [clj-example
      0
      ";; hiccup html"
      "[:div.some-class"
      " {:id \"foo\""
      "  :contentEditable true}"
      " \"Edit Me!\"]"]]

    [:div.fragment
     [clj-example
      0
      ";; harmonikit synth patch"
      "{:master-curves"
      "  {:release 0.0,"
      "   :decay -0.15301251,"
      "   :attack 0.52743465,"
      "   :fade -0.8029424},"
      "   :high-harmonics {:taper 0.015117645, :toggle 1.0},"
      "   :name \"Patch 42\","
      "   :master {:toggle 1.0}"
      " ... }"]]
    ]])

(defn immutability-intro1 []
  [:section
   [:h3 "Immutability"]
   [:div.fragment "is as safe as always copying your function's parameters and outputs"]
   [:div.fragment "but way faster"]
   [:img {:src "lib/img/persistentvector2.png"}]])

(defn immutability-intro2 []
  [:section
   [:h3 "Anything can act as a key, because"]

   ;; TODO split or replace
   [fragment-list
    :ul
    [:div
     "Every 'object' is also a value"
     [clj-example 0
      ";; even functions"
      "(defn hello-world [] \"Hello, world!\")"
      ";; #'user/hello-world"
      ";; same as (def (fn [] ...))"]]
    "Values have true equality"
    "Values Never Change (Immutability)"
    "Mutable objects aren't reliable keys, if their contents change, their hashcodes and equality change.  This breaks hashmaps and programs."]])

(defn immutability-questions []
  [:section
   [:h4 "How immutability affects programs"]
   [:section
    [:ul
     [:li.fragment
      "Code is easier to reuse and changes less when you do."]
     [:li.fragment
      "Effects are isolated and explicit"]]]
   [:section
    [:h4 "We can solve this in other ways"]
    [:ul
     [:li.fragment
      "Push down important business logic to one or many databases"]
     [:ul.fragment
      [:li "Not the right tools for every job."]
      [:li "Pay for it in infrastructure."]]
     [:li.fragment
      "Tear down and rebuild the whole program environment after every network request."]
     [:ul.fragment
      [:li "This can be bad for performance"]
      [:li "The sorts of large frameworks that do this don't compose well with each other and don't have all future use-cases covered"]]]]])

(defn compose-systems []
  [:section
   [:section
    [:h2 "1. Compose Systems"]
    [:small "Clojure encourages the composition of simple functions into more complex ones"]
    [clj-example
     0
     ";; function composition with comp (applied right to left)"
     ";; and partial function application with partial"
     "(def dec-and-double (comp (partial * 2) dec))"
     "(dec-and-double 3) ;; => 4"]]
   [:section
    [:small "functions are all generic and totally reusable"]
    [clj-example
     0
     "(defn nested-group-by
  [[kf & more :as kfs] coll]
  (if (seq kfs)
    (reduce (fn [acc [k vs]]
              (assoc acc k (nested-group-by more vs)))
            {}
            (group-by kf coll))
    coll))

(nested-group-by [even? (partial > 2) (partial > 4)] (range 10))

=> {true {true {true [0]},
          false {true [2],
                 false [4 6 8]}},
    false {true {true [1]},
           false {true [3],
                  false [5 7 9]}}}
"]]
   [:section
    [:small "functions are all generic and totally reusable 2"]
    [clj-example 0 "(defn random-time
  []
  (-> (java.util.Date.)
      .getTime
      rand
      long
      java.util.Date.
      .toInstant
      (.atZone (java.time.ZoneId/systemDefault))
      .toLocalDate))
"]]
   [:section
    [clj-example 0 "
(nested-group-by
  [#(.getYear %)
   #(.getMonthValue %)
   #(.getDayOfMonth %)]
  (take 10 (repeatedly random-time)))
"]
    [:div {:style {:font-size "80%"}}
     [clj-example 0 "
=> {1984 {6 {25 [#object[java.time.LocalDate 0x44a59c68 \"1984-06-25\"]]}},
    1973 {12 {19 [#object[java.time.LocalDate 0x13eff0c1 \"1973-12-19\"]]}},
    2011 {2 {28 [#object[java.time.LocalDate 0x16b37ac7 \"2011-02-28\"]]},
          9 {28 [#object[java.time.LocalDate 0x466db5f4 \"2011-09-28\"]]}},
    2001 {1 {18 [#object[java.time.LocalDate 0x7ca21cec \"2001-01-18\"]]}},
    2008 {5 {10 [#object[java.time.LocalDate 0x6003fef0 \"2008-05-10\"]]}},
    1994 {11 {5 [#object[java.time.LocalDate 0x595bdd13 \"1994-11-05\"]]}},
    1975 {1 {12 [#object[java.time.LocalDate 0x7a1594ca \"1975-01-12\"]]}},
    1999 {11 {8 [#object[java.time.LocalDate 0x6c992d3e \"1999-11-08\"]]}},
    2014 {8 {6 [#object[java.time.LocalDate 0x47a10f73 \"2014-08-06\"]]}}}
"]]]
    ])


(defn misc-examples []
  [:section
   [:section
    [:h1 "Code Examples"]]
   [:section
    "\n          What if, as a library author, you could just not write that\n          fluent interface code at all?\n          "
    [clj-example 0
     "\n(use 'clojure.string)\n\n;; These are equivalent\n\n(map trim (split (upper-case \"hola, world\") #\",\"))\n;; (\"HOLA\" \"WORLD\")\n\n(-> \"hola, world\"\n    upper-case\n    (split #\",\")\n    (->> (map trim)))\n;; (\"HOLA\" \"WORLD\")\n          "]
    [fragment-list :ul
     "Chaning can be implemented outside of the functions that use it"
     "It's a 'separate concern'"]]

   [:section
    "Functions don't care how you use them"
    [clj-example 0
     "(->> (range)\n     (filter even?)\n     (map (partial * 2))\n     (take 10)\n     (into []))\n;; [0 4 8 12 16 20 24 28 32 36]\n\n;; versus\n(into []\n      (take 10 (map (partial * 2)\n                    (filter even? (range)))))\n          "]
    [fragment-list :ul
     [:div "When you focus on input and output, you dont need:"
      [fragment-list :ul
       "builders"
       "mocks"
       "monkeypatching"]]]]
   [:section
    [:h3 "Macros"]
    "Let's look at a real one."
    [clj-example 0
     "(defmacro lazy-seq"
     "  [& body]"
     "  (list 'new 'clojure.lang.LazySeq"
     "        (list* 'fn* [] body)))"
     "  ;; simply returns a list, allocates a Java object (LazySeq)"
     "  ;; and-wraps your expressions in a function"
     ""
     "(macroexpand-1 '(lazy-seq ANYTHING1 ANYTHING2))"
     ""
     ";; => '(new clojure.lang.LazySeq (fn* [] ANYTHING1 ANYTHING2))"]
    [fragment-list :ul
     "The important part of clojure.lang.LazySeq is around 30 lines of Java"
     "The macro could have been written by anyone"]]

   [:section
    "Let's create an infinite sequence representing a square-wave"
    [:br]
    "\n          --__--__--__--__\n\n          "
    [clj-example 0
     "(defn osc [t cur-value so-far]"
     "  (let [so-far (mod so-far t)"
     "        next-val (if (zero? so-far)"
     "                   (- cur-value)"
     "    (cons next-val"
     "          (lazy-seq (osc t"
     "                         next-val"
     "                         (inc so-far))))))"
     ""
     "(defn square-wave"
     "  \"t is the period for a half-cycle\""
     "  [t]"
     "  (osc t 1 0))"
     ""
     "(take 10 (square-wave 3))"
     ";; (-1 -1 -1 1 1 1 -1 -1 -1 1)"
     ]

    [fragment-list :ul
     "This would require special syntax in most languages"
     "We just call the lazy-seq macro like a function"
     [:div "So the syntax doesn't deviate...  " [:span.fragment "It's just a list!"]]]]])


(defn slides-div []
  [:div.slides
   title-slide

   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
   ;; What's Good about the Language

   ;; function basics
   [js-2-clojure]
   everything-is-an-expression
   [calling-stuff]

   ;; smug lisp weenieism
   [:section
    [:h2 "This isn't an accident"]
    [:ul
     [:li.fragment "Javascript is 'Lisp in C's Clothing'"]
     [:li.fragment
      "Says Crockford: "
      [:a
       {:href "http://www.crockford.com/javascript/javascript.html"}
       "http://www.crockford.com/javascript/javascript.html"]]]]

   [:section
    [:h2 "Put another way..."]
    [:ul
     [:li
      "Q: Why do you think we've gotten so much mileage out of javascript?"]
     [:li.fragment "A: Lisp is very powerful, and it will never die"]]]

   ;; data
   [data-intro]
   [everything-is-data]


   ;; immutability
   [immutability-intro1]
   [immutability-intro2]
   [immutability-questions]

   [:section
    [:h2 "What We Really Want"]
    "Tools that let us"
    [fragment-list :ul
     "Compose Systems"
     "Change our minds"
     "Re-use components in different contexts, processes, servers, etc.."]]

   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
   ;; How it actually works

   [compose-systems]

   [:section
    [:h2 "2. Change Our Minds"]
    "Read-Eval-Print-Loop (REPL)"
    [fragment-list :ul
     "Read: (read-string \"(+ 1 2)\") => '(+ 1 2)"
     "Eval: (eval '(+ 1 2)) => 3"]
    [:div.fragment
     [clj-example
      0
      "\n(class (read-string \"(+ 1 2)\"))\n;; clojure.lang.PersistentList\n\n(map class (read-string \"(+ 1 2)\"))\n;; (clojure.lang.Symbol java.lang.Long java.lang.Long)\n          "]]]

   [:section
    [:h3 "REPL-Driven Development"]
    [:p "Lets you"]
    [fragment-list :ul
     "Redefine anything at runtime"
     "Test ideas by implementing them immediately"
     "Leverage rich editor integrations with tools like cider-emacs, cursive IDE, and vim-fireplace"]]

   [:section
    [:div "You can connect to a REPL anywhere"]
    [fragment-list :ul
     "over a network"
     "in production (unless you don't want to)"]]
   [:section
    [:div "It's not just for debugging..."]
    [:div.fragment "...It's core to the Lisp experience"]
    [:div.fragment "And is leveraged by all Clojure tooling"]]
   [:section
    [:div "And between reading and evaluation..."]
    [:h2.fragment "Macros"]
    [:div.fragment
     [clj-example 0
      "(defmacro infix
  \"Use this macro when you pine for the notation of your childhood\"
  [infixed]
  (list (second infixed) (first infixed) (last infixed)))"]]
    [fragment-list :ul
     "Written in Clojure, with the full power of the language"
     "No parsing"
     "More powerful than pure compiler macros (like in C)"
     "Can call other functions that have been defined to do their work"]]

   #_[:section
    [:h3 "Try it out!"]
    [:small "This toy ClojureScript REPL can evaluate arbitrary code."]
    [replet]]
   ;; maybe give them a repl to play with here

   [:section
    [:h2 "3. Re-Use Components"]
    [:p "Clojure is a hosted language, currently implemented in three environments:"]
    [fragment-list :ul
     "The Java Virtual Machine (JVM)"
     "JavaScript (ClojureScript)"
     ".NET Common Language Runtime (CLR)"]]

   [:section
    [:small "The Clojure core library is available on all three, but interop can differ:"]
    [clj-example 0
     ";; JVM"
     "(type \"foo\") ;; => java.lang.String"
     ";; JS"
     "(type \"foo\") ;; => #object[String \"function String() { [native code] }\"]"]

    [:div.fragment
     [:small "With Reader Conditionals, we can write Clojure source for multiple targets in a single source file."]
     [clj-example 0
      ";; dates
 (.getTime
   #?(:clj (java.util.Date.)
      :cljs (js/Date.)))
;; uris
#?(:clj
   (defn url-encode
    [string]
    (some-> string
            str
            (URLEncoder/encode \"UTF-8\")
            (.replace \"+\" \"%20\")))
   :cljs
   (defn url-encode
    [string]
    (some-> string
            str
            (js/encodeURIComponent)
            (.replace \"+\" \"%20\"))))"]]
    [:div.fragment "We can compose functions into systems, across platforms"]
    ]
   [:section
    [:div "We can use any library from the host language"]
    [fragment-list :ul
     "Joda Time"
     "processing"
     "overtone"
     "React"
     "Dl4j"
     "Three.js"
     ]
    [:div.fragment "Which is empowering"]]

   [misc-examples]

   [:section
    [:h3 "Resources"]
    [:div
     "Clojure: "
     [:a {:href "http://clojure.org"} "http://clojure.org"]]
    [:div
     "Fun Exercises: "
     [:a
      {:href "http://www.4clojure.com"}
      "http://www.4clojure.com"]]
    [:div
     "Cheatsheets: "
     [:a
      {:href "http://clojure.org/cheatsheet"}
      "http://clojure.org/cheatsheet"]]
    [:div
     "Building: "
     [:a
      {:href "https://github.com/technomancy/leiningen"}
      "https://github.com/technomancy/leiningen"]]
    [:div
     "Insight: "
     [:a
      {:href "http://www.youtube.com/user/ClojureTV"}
      "http://www.youtube.com/user/ClojureTV"]]
    [:div
     "Community docs: "
     [:a {:href "http://clojuredocs.org"} "http://clojuredocs.org"]]
    [:div
     "Blogs: "
     [:a
      {:href "http://planet.clojure.in"}
      "http://planet.clojure.in"]]
    [:div
     "Light Table: "
     [:a
      {:href "http://www.lighttable.com"}
      "http://www.lighttable.com"]]
    [:div
     "this doc: "
     [:a
      {:href "http://gtrak.github.io/bohconf.clojure"}
      "http://gtrak.github.io/bohconf.clojure"]]
    [:div
     "Clojure for Rubyists"
     [:a
      {:href "https://www.youtube.com/watch?v=QmsYWSz1jsk"}
      "https://www.youtube.com/watch?v=QmsYWSz1jsk"]]]])
