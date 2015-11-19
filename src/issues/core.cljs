(ns issues.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch-sync]]
            [issues.handlers]
            [issues.subs]
            [issues.views.movie :refer [movies-cp]]))

(enable-console-print!)

;; -------------------- min profile, for offline bundle, release --------------------

;; (.registerRunnable (.-AppRegistry js/React) "issues"
;;                    (fn [params]
;;                      (r/render [movies-cp] (.-rootTag params))))

;; -------------------- dev profile --------------------

(r/render [movies-cp] 1)

(defn ^:export init []
  (dispatch-sync [:initialize-db])
  ((fn render []
     (.requestAnimationFrame js/window render))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  )
