(ns main.subs
  (:require
    [clojure.string]
    [main.validator :as vd]
    [re-frame.core :as rf]))

(rf/reg-sub
  ::registered?
  (fn [db _]
    (get db :registered?)))

(rf/reg-sub
  ::form
  (fn [db [_ id]]
    (get-in db [:form id] "")))

(rf/reg-sub
  ::valid-input?
  (fn [_ [_ type value]]
    (when-not (clojure.string/blank? type)
      (vd/valid-email-or-tel? type value))))