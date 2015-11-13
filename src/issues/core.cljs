(ns issues.core
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

(enable-console-print!)

(def styles {:fullscreen {:position "absolute"
                          :top 0
                          :left 0
                          :bottom 0
                          :right 0}
             :green {:color "#00ff00"}
             :viewbg {:padding 10
                      :background-color "#ffffff"}
             :input {:height 35
                     :border-color "gray"
                     :border-width 1
                     :padding-left 10
                     :border-radius 5
                     :margin 10}})

(def global-state (r/atom 0))

(declare page-comp)
(declare row-comp)

(def ds (React.ListView.DataSource. #js{:rowHasChanged (fn[a b] false)}))

(defn list-view-source [v]
  (let [res #js[]]
    (doseq [item v]
      (.push res (r/atom item)))
    (r/atom (js->clj res))))

(def rows (list-view-source (clj->js (range 100))))

(def current-tab (r/atom "tab1"))

(defn page[{nav :navigator}]
  [tabs
   [tabs-item {:title "tab1" :on-press #(reset! current-tab "tab1") :selected (= "tab1" @current-tab)}
    [scroll {:always-bounce-vertical true
             :bounces true
             :style (:fullscreen styles)}
     [view {:style [(:viewbg styles) {:flexDirection "column"}]}
      [text @global-state]
      [text {:style (:green styles)} "native"]
      [input {:style (:input styles)
              :value @global-state
              :on-change-text #(reset! global-state %)}]
      [slider {:style {:width 200}
               :on-value-change #(reset! global-state %)}]
      [image {:source {:uri "https://assets-cdn.github.com/images/modules/microsoft_callout/corner.png"}
              :style {:width 306 :height 104}}]

      [list-view {:dataSource (.cloneWithRows ds (clj->js @rows))
                  :render-row (fn[row]
                                (js/React.createElement row-comp #js{:row row}))
                  :style {:left 0 :right 0 :height 250 :border-width 1 :border-color "#000"}}]

      [switch {:on-value-change #(.push nav #js{:title "new" :component page-comp})}]
      [text {:on-press #(swap! (get @rows 99) inc)} "click update list"]
      [text {:on-press #(swap! rows conj (r/atom 100))} "click add list"]]]]
   [tabs-item {:title "tab2" :selected (= "tab2" @current-tab) :on-press #(reset! current-tab "tab2")}
    [text {:style {:top 100}} "tab2"]]
   [tabs-item {:title "tab3" :selected (= "tab3" @current-tab) :on-press #(reset! current-tab "tab3")}
    [text {:style {:top 100}} "tab3"]]])

(def page-comp (r/reactify-component page))
(def row-comp (r/reactify-component (fn[props]
                                      (let [row (props :row)]
                                        (print props)
                                        [touchable-highlight {:style {:border-top-width 1 :border-color "#000"} :on-press #(alert "list" (str @row))}
                                         [text @row]]))))


(defn root []
  [navigator {:initial-route {:title "App4" :component page-comp} :style (:fullscreen styles)}])

;; -------------------- min profile, for offline bundle, release --------------------

;; (.registerRunnable (.-AppRegistry js/React) "issues"
;;                    (fn [params]
;;                      (r/render [root] (.-rootTag params))))

;; -------------------- dev profile --------------------
(r/render [root] 1)

(defn ^:export init []
  ((fn render []
     (.requestAnimationFrame js/window render))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  )
