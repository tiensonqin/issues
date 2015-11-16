(defproject issues "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.1" :exclusions [cljsjs/react]]
                 [cljs-http "0.1.37" :exclusions [org.clojure/clojure
                                                  org.clojure/clojurescript]]
                 [prismatic/schema "1.0.1"]

                 ;; piggieback
                 [com.cemerick/piggieback "0.2.1"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-SNAPSHOT"]
            [org.clojure/clojurescript "1.7.170"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]

              :figwheel {:on-jsload "issues.core/on-js-reload"
                         :heads-up-display false
                         :debug false}

              :compiler {:main issues.core
                         :asset-path "target/out"
                         :output-to "target/issues.js"
                         :output-dir "target/out"
                         :source-map-timestamp true}}
             {:id "min"
              :source-paths ["src"]
              :compiler {:output-to "index.ios.js"
                         :output-dir "target/build"
                         :optimizations :simple
                         :pretty-print true}}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             ;; :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             :nrepl-port 7888

             ;; Load CIDER, refactor-nrepl and piggieback middleware
             :nrepl-middleware ["cider.nrepl/cider-middleware"
                                "refactor-nrepl.middleware/wrap-refactor"
                                "cemerick.piggieback/wrap-cljs-repl"]

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             })
