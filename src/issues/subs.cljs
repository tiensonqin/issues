(ns issues.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]
            [issues.db :as d]
            [re-frame.subs :as subs]))

(defn init-subs!
  []
  (subs/clear-handlers!)
  (doseq [[k v] d/default-db]
    (rf/register-sub
     k
     (fn [db ks]
       (reaction (get-in @db ks))))))

(init-subs!)
