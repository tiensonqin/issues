(ns issues.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs-http.util :refer [json-decode]]
            [re-frame.core :refer [dispatch]]
            [cljs.core.async :refer [<!] :as async]))


;; -------------------- HTTP --------------------

;; (def base-uri (:base-uri config))
;; (def api (str base-uri "/v1/"))

;; TODO ring middlewares
(defn request
  [verb uri {:keys [on-success on-error query-params json-params multipart-params headers xsrf-token?
                    on-401 on-500]
             :as req
             :or {on-success println
                  ;; on-error #(dispatch [:set-error-notices %])
                  on-401 #(println "redirect to login")
                  on-500 #(println "Wrong api request 500: " uri)}}]
  (let [http-fn (case verb
                  :get    http/get
                  :post   http/post
                  :delete http/delete
                  :patch  http/patch)]
    (go (let [{:keys [status success body error-code error-text] :as response}
              (<!
               (http-fn uri
                        (cond-> req
                          ;; xsrf-token? (assoc-in [:headers "x-xsrf-token"] (cookies/get :XSRF-TOKEN))
                          req (dissoc :on-success :on-error :xsrf-token?))))]
          (cond
            (true? success)
            (if on-success (on-success body))

            (= 400 status)
            (let [msgs (:message body)
                  msgs (if (map? msgs) (vals msgs) msgs)]
              (dispatch [:set-notice :header :danger msgs]))

            (and (= 401 status) (= "access denied (jwt token is absent or invalid)" body))
            (on-401)

            (= 500 status)
            (on-500)

            :else
            (println "Wrong api request: " uri " status: " status))))))


(def GET (partial request :get))
(def PATCH (partial request :patch))
(def DELETE (partial request :delete))
(def POST (partial request :post))

;; (defn- wrap-opts
;;   ([opts]
;;    (wrap-opts nil opts))
;;   ([m opts]
;;    (merge m (apply hash-map opts))))

;; ;; -------------------- Users --------------------
;; (defn authenticate
;;   [name password & opts]
;;   (POST (str base-uri "/auth/token")
;;     (wrap-opts {:json-params {:identity name
;;                               :password password}}
;;                opts)))

;; (defn get-current-user
;;   [& opts]
;;   (GET (str api "me")
;;     (wrap-opts opts)))

;; (defn create-user
;;   [entity & opts]
;;   (POST (str api "users")
;;     (wrap-opts {:json-params entity
;;                 :xsrf-token? true}
;;                opts)))

;; (defn get-all-users
;;   [& opts]
;;   (GET (str api "users")
;;     (wrap-opts {:query-params
;;                 {:direction "asc"
;;                  :limit 1000}}
;;                opts)))

;; (defn update-profile
;;   [entity & opts]
;;   (PATCH (str api "me")
;;          (wrap-opts {:json-params entity} opts)))

;; ;; -------------------- Cursor --------------------
;; (defn cursor
;;   [url & opts]
;;   (GET url (wrap-opts opts)))

;; ;; -------------------- Issues --------------------
;; (defn get-issues
;;   ([state]
;;    (get-issues nil state))
;;   ([user-id issue-state & opts]
;;    (GET (str api "issues")
;;      (wrap-opts {:query-params
;;                  (cond-> {}
;;                    (some? user-id)
;;                    (assoc :user_id user-id)

;;                    (some? issue-state)
;;                    (assoc :state (name issue-state)))}
;;                 opts))))

;; (defn get-all-issues
;;   [& opts]
;;   (GET (str api "issues")
;;     (wrap-opts opts)))

;; (defn get-search-issues
;;   [q & opts]
;;   (GET (str api "issues/search")
;;     (wrap-opts {:query-params {:q q}} opts)))

;; (defn get-issue
;;   [id & opts]
;;   (GET (str api "issues/" id)
;;     (wrap-opts opts)))

;; (defn update-issue
;;   [id entity & opts]
;;   (PATCH (str api "issues/" id)
;;          (wrap-opts {:json-params entity}
;;                     opts)))

;; (defn take-issue
;;   [id & opts]
;;   (PATCH (str api "issues/" id "/take")
;;     (wrap-opts opts)))

;; (defn submit-issue
;;   [id & opts]
;;   (PATCH (str api "issues/" id "/submit")
;;          (wrap-opts opts)))

;; (defn assign-issue
;;   [id assignee deadline & opts]
;;   (PATCH (str api "issues/" id "/assign")
;;          (wrap-opts
;;           {:json-params {:assignee assignee
;;                          :deadline deadline}}
;;           opts)))

;; (defn create-issue
;;   [entity & opts]
;;   (POST (str api "issues")
;;     (wrap-opts {:json-params entity
;;                 :xsrf-token? true}
;;                opts)))

;; (defn review-issue
;;   [id entity & opts]
;;   (PATCH (str api "issues/" id "/review")
;;          (wrap-opts {:json-params entity}
;;                     opts)))

;; (defn delete-issue
;;   [id & opts]
;;   (DELETE (str api "issues/" id)
;;     (wrap-opts opts)))

;; ;; -------------------- Comments --------------------
;; (defn get-issue-comments
;;   [issue-id & opts]
;;   (GET (str api "issues/" issue-id "/comments")
;;     (wrap-opts opts)))

;; (defn get-comment
;;   [id & opts]
;;   (GET (str api "comments/" id)
;;     (wrap-opts opts)))

;; (defn update-comment
;;   [id entity & opts]
;;   (PATCH (str api "comments/" id)
;;          (wrap-opts {:json-params entity} opts)))

;; (defn create-comment
;;   [entity & opts]
;;   (POST (str api "comments")
;;     (wrap-opts {:json-params entity
;;                 :xsrf-token? true} opts)))

;; (defn delete-comment
;;   [id & opts]
;;   (DELETE (str api "comments/" id)
;;     (wrap-opts opts)))

;; ;; -------------------- Attachment --------------------
;; (defn get-issue-attachments
;;   [issue-id & opts]
;;   (GET (str api "issues/" issue-id "/attachments")
;;     (wrap-opts opts)))

;; (defn create-attachment
;;   [entity & opts]
;;   (POST (str api "attachments")
;;     (wrap-opts {:json-params entity
;;                 :xsrf-token? true} opts)))

;; (defn delete-attachment
;;   [id & opts]
;;   (DELETE (str api "attachments/" id)
;;     (wrap-opts opts)))

;; ;; -------------------- File upload --------------------

;; ;; TODO what if network down?
;; (defn upload
;;   [attachment & opts]
;;   (let [file (.-file attachment)
;;         form-data (http/generate-form-data {:file file})
;;         xhr (js/XMLHttpRequest.)
;;         api-url (str api "pictures/upload")]
;;     (.open xhr "POST" api-url true)
;;     (set! (.-withCredentials xhr) true)
;;     (.setRequestHeader xhr "x-xsrf-token" (cookies/get :XSRF-TOKEN))
;;     (set! (.-onload xhr) (fn []
;;                            (if (= 200 (.-status xhr))
;;                              (let [url (-> xhr
;;                                            .-responseText
;;                                            json-decode
;;                                            :url)
;;                                    url (str url "!300x300")]
;;                                (.setAttributes attachment
;;                                                (clj->js
;;                                                 {:url url
;;                                                  :href url}))))))

;;     (set! (.. xhr -upload -onprogress) (fn [event]
;;                                          (->> (/ (.-loaded event) (.-total event))
;;                                               (* 100)
;;                                               (.setUploadProgress attachment))))
;;     (.send xhr form-data)))

;; (defn upload-attachment
;;   [file-name file success-cb error-cb]
;;   (let [fd (js/FormData.)
;;         xhr (js/XMLHttpRequest.)
;;         api-url (str api "pictures/upload")]
;;     (.append fd "file" file file-name)
;;     (.open xhr "POST" api-url true)
;;     (set! (.-withCredentials xhr) true)
;;     (.setRequestHeader xhr "x-xsrf-token" (cookies/get :XSRF-TOKEN))
;;     (set! (.-onload xhr) (fn []
;;                            (if (= 200 (.-status xhr))
;;                              (let [url (-> xhr
;;                                            .-responseText
;;                                            json-decode
;;                                            :url)]
;;                                (success-cb url))
;;                              (error-cb xhr))))
;;     (.send xhr fd)))
