(ns issues.core
  (:require [reagent.core :as r :refer [atom]]
            [issues.util :refer [text view image touchable-highlight
                                 navigator-ios scroll input switch list-view
                                 slider tabs tabs-item alert
                                 row-has-changed]]
            ;; [issues.styles :refer [styles]]
            [issues.http :as http]
            [cljs-http.util :refer [json-decode]]))

(enable-console-print!)

(def request-url "https://raw.githubusercontent.com/facebook/react-native/master/docs/MoviesExample.json")

(def styles {:container {:flex 1
                         :flexDirection "row"
                         :justify-content "center"
                         :align-items "center"
                         :background-color "#F%FCFF"}
             :list-view {:padding-top 20
                         :background-color "#F5FCFF"}
             :thumbnail {:width 53
                         :height 81}
             :right-container {:flex 1}
             :title {:font-size 20
                     :margin-bottom 8
                     :text-align "center"}
             :year {:text-align "center"}})

(defn movie-cp
  [movie]
  [view {:style (:container styles)}
   [image {:source {:uri (get-in movie [:posters :thumbnail])}
           :style (:thumbnail styles)}]

   [view {:style (:right-container styles)}
    [text {:style (:title styles)}
     (:title movie)]
    [text {:style (:year styles)}
     (:year movie)]]])

(defn movies-cp
  [movies]
  [list-view {:dataSource (-> (js/React.ListView.DataSource. #js{:rowHasChanged row-has-changed})
                              (.cloneWithRows (clj->js (:movies movies))))
              :render-row (fn [row]
                            (r/as-element (movie-cp (js->clj row :keywordize-keys true))))
              :style (:list-view styles)}])

(defn loading-cp
  []
  [view {:style (:container styles)}
   [text "Loading movies..."]])

(defn root-cp
  []
  (let [movies (r/atom nil)]
    (http/GET request-url {:on-success (fn [result]
                                         (reset! movies (json-decode result)))})
    (fn []
      (if @movies
        [movies-cp @movies]

        [loading-cp]))))

;; (def global-state (r/atom 0))

;; (declare page-comp)

;; (def rows (list-view-source (clj->js (range 100))))

;; (def current-tab (r/atom "tab1"))

;; (defn row-comp
;;   [row]
;;   [touchable-highlight {:style {:border-top-width 1 :border-color "#000"} :on-press #(alert "list" (str row))}
;;    [text row]])

;; (defn page[{nav :navigator}]
;;   [tabs
;;    [tabs-item {:title "tab1" :on-press #(reset! current-tab "tab1") :selected (= "tab1" @current-tab)}
;;     [scroll {:always-bounce-vertical true
;;              :bounces true
;;              :style (:fullscreen styles)}
;;      [view {:style [(:viewbg styles) {:flexDirection "column"}]}
;;       [text @global-state]
;;       [text {:style (:green styles)} "native"]
;;       [input {:style (:input styles)
;;               :value @global-state
;;               :on-change-text #(reset! global-state %)}]
;;       [slider {:style {:width 200}
;;                :on-value-change #(reset! global-state %)}]
;;       [image {:source {:uri "https://assets-cdn.github.com/images/modules/microsoft_callout/corner.png"}
;;               :style {:width 306 :height 104}}]

;;       [list-view {:dataSource (.cloneWithRows data-source (clj->js @rows))
;;                   :render-row (fn [row] (r/as-element [row-comp @row]))
;;                   :style {:left 0 :right 0 :height 250 :border-width 1 :border-color "#000"}}]

;;       [switch {:on-value-change #(.push nav #js{:title "new" :component page-comp})}]
;;       [text {:on-press #(swap! (get @rows 99) inc)} "click update list"]
;;       [text {:on-press #(swap! rows conj (r/atom 100))} "click add list"]]]]
;;    [tabs-item {:title "tab2" :selected (= "tab2" @current-tab) :on-press #(reset! current-tab "tab2")}
;;     [text {:style {:top 100}} "tab2"]]
;;    [tabs-item {:title "tab3" :selected (= "tab3" @current-tab) :on-press #(reset! current-tab "tab3")}
;;     [text {:style {:top 100}} "tab3"]]])

;; (def page-comp (r/reactify-component page))

;; (defn root []
;;   [navigator {:initial-route {:title "Hello world" :component page-comp} :style (:fullscreen styles)}])

;; -------------------- min profile, for offline bundle, release --------------------

;; (.registerRunnable (.-AppRegistry js/React) "issues"
;;                    (fn [params]
;;                      (r/render [root] (.-rootTag params))))

;; -------------------- dev profile --------------------
(r/render [root-cp] 1)

(defn ^:export init []
  ((fn render []
     (.requestAnimationFrame js/window render))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  )
