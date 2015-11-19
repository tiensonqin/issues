(ns issues.views.movie
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [issues.util :refer [text view image touchable-highlight
                                 navigator-ios scroll input switch list-view
                                 slider tabs tabs-item alert
                                 row-has-changed]]
            [issues.styles :refer [styles]]))

(defn loading-cp
  []
  [view {:style (:container styles)}
   [text "Loading movies..."]])

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
  []
  (let [movies (subscribe [:movies])]
    (dispatch [:get-movies])
    (fn []
      (if @movies
        [list-view {:dataSource (-> (js/React.ListView.DataSource. #js{:rowHasChanged row-has-changed})
                                    (.cloneWithRows (clj->js (:movies @movies))))
                    :render-row (fn [row]
                                  (r/as-element (movie-cp (js->clj row :keywordize-keys true))))
                    :style (:list-view styles)}]

        [loading-cp]))))
