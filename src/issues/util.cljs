(ns issues.util
  (:require [reagent.core :as r]))

(set! js/React (js/require "react-native/Libraries/react-native/react-native.js"))
;; work for om/next

;; (defn adapt-react-element
;;   [elem]
;;   (fn [opts & children]
;;     (apply js/React.createElement elem (clj->js opts) children)))

;; (def view (adapt-react-element js/React.View))
;; (def text (adapt-react-element js/React.Text))
;; (def image (adapt-react-element js/React.Image))
;; (def touchable-highlight (adapt-react-element js/React.TouchableHighlight))
;; (def navigator-ios (adapt-react-element js/React.NavigatorIOS))
;; (def scroll (adapt-react-element js/React.ScrollView))
;; (def input (adapt-react-element js/React.TextInput))
;; (def switch (adapt-react-element js/React.SwitchIOS))
;; (def list-view (adapt-react-element js/React.ListView))
;; (def slider (adapt-react-element js/React.SliderIOS))
;; (def tabs (adapt-react-element js/React.TabBarIOS))
;; (def tabs-item (adapt-react-element js/React.TabBarIOS.Item))

;; work for reagent
(def text (r/adapt-react-class (.-Text js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def image (r/adapt-react-class (.-Image js/React)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight js/React)))
(def navigator-ios (r/adapt-react-class (.-NavigatorIOS js/React)))
(def navigator (r/adapt-react-class (.-Navigator js/React)))
(def scroll (r/adapt-react-class (.-ScrollView js/React)))
(def input (r/adapt-react-class (.-TextInput js/React)))
(def switch (r/adapt-react-class (.-SwitchIOS js/React)))
(def list-view (r/adapt-react-class (.-ListView js/React)))
(def slider (r/adapt-react-class (.-SliderIOS js/React)))
(def tabs (r/adapt-react-class (.-TabBarIOS js/React)))
(def tabs-item (r/adapt-react-class (.-TabBarIOS.Item js/React)))

(defn alert[title content]
  (.alert (.-AlertIOS js/React) title content))

(defn row-has-changed [x y]
  (let [row-1 (js->clj x :keywordize-keys true) row-2 (js->clj y :keywordize-keys true)]
    (not= row-1 row-2)))
