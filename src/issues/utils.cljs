(ns issues.utils
  (:require [reagent.core :as r :refer [atom]]))

(def data-source (js/React.ListView.DataSource. #js{:rowHasChanged (fn [a b] false)}))

(defn list-view-source [v]
  (let [res #js[]]
    (doseq [item v]
      (.push res (r/atom item)))
    (r/atom (js->clj res))))
