(ns main.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [main.db :as db]
    [re-frame.core :as rf]))

(rf/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
    db/default-db))

(rf/reg-event-db
  ::update-form
  (fn [db [_ id val]]
    (assoc-in db [:form id] val)))

(rf/reg-event-db
  ::register!
  (fn [db [_]]
    (assoc db :registered? true)))
