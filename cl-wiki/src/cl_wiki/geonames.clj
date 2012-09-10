(ns cl-wiki.geonames
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def max-distance 1)

(defn get-url [term]
  (let [api-source "http://api.geonames.org/wikipediaSearchJSON?maxRows=10&username=demo&"]
    (client/get (str api-source (client/generate-query-string { "q" term })))))

(defn get-json [term]
  (:geonames (json/read-json ((get-url term) :body))))

(defn get-point [result] { :lat (:lat result) :lon (:lng result) })
(defn degree-to-rad [degrees] (* degrees (/ Math/PI 180)))
(defn haversine-distance [ coor1 coor2 ]
  ; return the haversine distance between two lat/lon
  ; coordinates in kilometers
  (let [lat1 (degree-to-rad (:lat coor1)) lon1 (degree-to-rad (:lon coor1))
        lat2 (degree-to-rad (:lat coor2)) lon2 (degree-to-rad (:lon coor2))]
    (let [ delta-lat (- lat1 lat2) delta-lon (- lon1 lon2) r 6371]
       ; a = sin²(δlat/2) + cos(lat1).cos(lat2).sin²(δlong/2)
      (let [ a (+ (Math/pow (/ (Math/sin delta-lat) 2) 2)
                  (* (Math/cos lat1) (Math/cos lat2)
                     (Math/pow (/ (Math/sin delta-lon) 2) 2)))]
       ; c = 2.atan2(√a, √(1−a))
        (let [ c (* 2 (Math/atan2 (Math/sqrt a) (Math/sqrt (- 1 a))))]
          (* r c))))))

(defn sort-by-distance [ origin results ]
  (sort #(compare (haversine-distance origin (get-point %1))
                  (haversine-distance origin (get-point %2))) results))

(defn filter-by-distance [ origin results max-distance ]
  ; Return only results within `max-distance` km from origin
  (filter #(< (haversine-distance origin (get-point %)) max-distance) results))

(defn compute-distances [ origin results ]
  ; Return only results within `max-distance` km from origin
  (map #(haversine-distance origin (get-point %)) results))

(defn print-place [place]
  (str (:title place) "\n" (:wikipediaUrl place) "\n" (:summary place)))

(defn best-match [origin results] (first (sort-by-distance origin results)))

(defn best-match [origin results max-distance]
  (first
   (sort-by-distance origin (filter-by-distance origin results max-distance))))

(defn search [origin term]
  (let [ json-response (get-json term) ]
    (print-place (best-match origin json-response max-distance))))
