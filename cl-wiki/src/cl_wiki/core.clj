(ns cl-wiki.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [cl-wiki.geonames :as geonames])
  (:use noir.core)
  (:use hiccup.core)
  (:use hiccup.page)
  (:require [noir.server :as server]))

(defpartial layout [& content]
  (html5
   [:head
    [:title "Geonames lookup"]
    (include-css "/css/style.css")]
   [:body
    content]))

(defpage "/" []
  (layout
  "<h1>Welcome!</h1>"))

(defpage "/search" {:keys [lat lon query]}
  (html
   [:h1 "Welcome!"]
   [:p "Origin: (" lat "," lon ")"]
   [:p "Query: " query]
   [:p "Response: " [:br]
    (clojure.string/replace (geonames/search {:lat (Float/parseFloat lat)
                      :lon (Float/parseFloat lon)} query) "\n" "<br />")]))

(defn -main
  "I don't do a whole lot."
  [& args]
  (server/start 8080))
