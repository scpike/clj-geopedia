(ns cl-wiki.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [cl-wiki.geonames :as geonames])
  (:use noir.core)
  (:use hiccup.core)
  (:use hiccup.page)
  (:use hiccup.form)
  (:use hiccup.element)
  (:require [noir.server :as server]))

(defpartial layout [& content]
  (html5
   [:head
    [:title "Geonames lookup"]
    (include-css "/css/style.css")
    (include-js "/js/jquery-1.8.2.js")
    (include-js "/js/base.js")]
   [:body
    content]))

(defpartial geo-fields []
  (label "lat" "Latitude:")
  (text-field "lat")
  (label "lon" "Longitude:")
  (text-field "lon")
  (label "query" "Query:")
  (text-field "query"))

(defpage "/" []
  (layout
   [:h1 "Welcome!"]
   (form-to { :id "geo" } [:get "/search"]
            (geo-fields)
            (submit-button "Find"))))

(defn simple-format [string]
  (clojure.string/replace string "\n" "<br />"))

(defpage "/search" {:keys [lat lon query]}
  (layout
   [:h1 "Welcome!"]
   [:p "Origin: (" lat "," lon ")"]
   [:p "Query: " query]
   (let [response (geonames/search {:lat (Float/parseFloat lat)
                                    :lon (Float/parseFloat lon)} query)]
     [:div
      [:h2 (link-to (response :wikipediaUrl) (response :title))]
      (image (response :thumbnailImg))
      [:p (response :summary)]])))

(defpage "/foursquare_search" {:keys [lat lon query]}
  (layout
   [:h1 "Welcome!"]
   [:p "Origin: (" lat "," lon ")"]
   [:p "Query: " query]
   (let [response (geonames/search {:lat (Float/parseFloat lat)
                                    :lon (Float/parseFloat lon)} query)]
     [:div
      [:h2 (link-to (response :wikipediaUrl) (response :title))]
      (image (response :thumbnailImg))
      [:p (response :summary)]])))

(defn -main
  "I don't do a whole lot."
  [& args]
  (server/start 8080))
