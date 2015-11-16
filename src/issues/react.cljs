(ns issues.react
  (:require [reagent.core :as r :refer [atom]]))

(set! js/React (js/require "react-native/Libraries/react-native/react-native.js"))

(def text (r/adapt-react-class (.-Text js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def image (r/adapt-react-class (.-Image js/React)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight js/React)))
(def navigator (r/adapt-react-class (.-NavigatorIOS js/React)))
(def scroll (r/adapt-react-class (.-ScrollView js/React)))
(def input (r/adapt-react-class (.-TextInput js/React)))
(def switch (r/adapt-react-class (.-SwitchIOS js/React)))
(def list-view (r/adapt-react-class (.-ListView js/React)))
(def slider (r/adapt-react-class (.-SliderIOS js/React)))
(def tabs (r/adapt-react-class (.-TabBarIOS js/React)))
(def tabs-item (r/adapt-react-class (.-TabBarIOS.Item js/React)))

(defn alert[title content]
  (.alert (.-AlertIOS js/React) title content))
