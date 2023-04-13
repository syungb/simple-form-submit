(ns main.core
  (:require
    [main.events :as events]
    [main.views :as views]
    [reagent.dom :as rd]
    [re-frame.core :as rf]))

(defn ^:dev/after-load start
  []
  (rf/clear-subscription-cache!)
  (rd/render
    [views/main]
    (js/document.getElementById "app")))

(defn ^:export init
  []
  (rf/dispatch-sync [::events/initialize-db])
  (start))