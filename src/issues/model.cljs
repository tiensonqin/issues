(ns issues.model
  (:require [issues.http :refer [GET POST PATCH DELETE]]))

(def request-url "https://raw.githubusercontent.com/facebook/react-native/master/docs/MoviesExample.json")

(defn- wrap-opts
  ([opts]
   (wrap-opts nil opts))
  ([m opts]
   (merge m (apply hash-map opts))))

(defn get-movies
  [& opts]
  (GET request-url (wrap-opts opts)))
