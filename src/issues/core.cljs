(ns issues.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch-sync]]
            [issues.handlers]
            [issues.subs]
            [issues.views.movie :refer [movies-cp]]))

;; React Native should set DEV to false when bundling with --minify
;;   if you're getting App Registration errors in production, just set this to false
(def developMode? js/__DEV__)

(enable-console-print!)

(defn runDevelopment []
  (dispatch-sync [:initialize-db])
  (r/render [movies-cp] 1)
  (defn ^:export init []
    ((fn render []
       (.requestAnimationFrame js/window render)))))

(defn runProduction []
  (dispatch-sync [:initialize-db])
  (.registerRunnable (.-AppRegistry js/React) "figTest"
                     (fn [params]
                       (r/render [root] (.-rootTag params)))))
(case developMode?
  true (runDevelopment)
  (runProduction))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  )
