(ns cl-wiki.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [cl-wiki.geonames :as geonames])
  (:use noir.core)
  (:require [noir.server :as server]))

(defpage "/welcome" []
  "<h1>Welcome!</h1>")

(defpage "/search" {:keys [lat lon query]}
  (html
   [:h1 "Welcome!"]
   [:h2 "Welcome you!"]
   [:p "Origin: (" lat "," lon ")"]
   [:p "Query: " query]
   [:p "Response: " [:br]
    (clojure.string/replace (geonames/search {:lat (Float/parseFloat lat)
                      :lon (Float/parseFloat lon)} query) "\n" "<br />")]))

(defpage "/search" {:keys [lat lon query]}
  (html
   [:h1 "Welcome!"]
   [:h2 "Welcome you!"]
   [:p "Origin: (" lat "," lon ")"]
   [:p "Query: " query]
   [:p "Response: " [:br]
    (clojure.string/replace (geonames/search {:lat (Float/parseFloat lat)
                      :lon (Float/parseFloat lon)} query) "\n" "<br />")]))

; (server/start 8080)

(defn -main
  "I don't do a whole lot."
  [& args]
  (println (geonames/search {:lat 40.773834 :lon  -73.871482 } "Laguardia")))
