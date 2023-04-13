(ns main.views
  (:require
    [clojure.string]
    [main.events :as events]
    [main.subs :as subs]
    [re-frame.core :as rf]))

;; UI Components

(defn header-text
  [text]
  [:h1
   {:class "mt-6 mb-3 text-center text-3xl font-extrabold text-gray-900 mb-2.5"}
   text])

(defn label-text
  [text]
  [:label
   {:class "block mb-2 text-sm text-gray-700"}
   text])

(defn select
  [{:keys [label options value on-change]}]
  [:div
   {:class "my-3"}
   [label-text label]
   [:select
    {:class     "w-full mb-2.5 px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
     :value     value
     :on-change on-change}
    [:option
     {:value ""}
     "Please select"]
    (doall
      (for [{:keys [text type]} options]
        ^{:key text}
        [:option {:value type} text]))]])

(defn input
  [{:keys [type label value placeholder on-change]}]
  [:div
   {:class "flex flex-col my-3"}
   [label-text label]
   [:input
    {:class       "px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm mb-2.5"
     :type        type
     :value       value
     :placeholder (or placeholder "")
     :on-change   on-change}]])

(defn button
  [{:keys [type disabled on-click text]
    :or   {type :button}}]
  (let [button-color-class (case type
                             :submit "bg-green-600 hover:bg-green-700 focus:ring-green-500"
                             :button "bg-blue-500 hover:bg-blue-600 focus:ring-blue-400")]
    [:button
     {:class    ["flex justify-center w-full my-3 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white focus:outline-none focus:ring-offset-2 disabled:bg-gray-500"
                 button-color-class]
      :type     type
      :disabled disabled
      :on-click on-click}
     text]))

;; Views

(def register-select-options
  [{:text        "email"
    :type        "email"
    :placeholder "example@example.com"}
   {:text        "phone number"
    :type        "tel"
    :placeholder "+12345678910"}])

(defn register-form
  [_]
  (fn [{:keys [selected-type typed-value]}]
    (let [valid? (rf/subscribe [::subs/valid-input? selected-type typed-value])
          {:keys [text placeholder]} (->
                                       (filter #(= selected-type (:type %)) register-select-options)
                                       first)]
      [:div
       [header-text "User Register Form"]
       [select
        {:label     "Please select register type:"
         :value     selected-type
         :options   register-select-options
         :on-change #(do
                       (rf/dispatch [::events/update-form :selected-type (.. % -target -value)])
                       (rf/dispatch [::events/update-form :input-value ""]))}]
       (when-not (clojure.string/blank? selected-type)
         [:<>
          [input
           {:label       (str "Please type your " text ":")
            :value       typed-value
            :type        selected-type
            :placeholder placeholder
            :on-change   #(rf/dispatch [::events/update-form :input-value (.. % -target -value)])}]
          [button
           {:type     :submit
            :disabled (not @valid?)
            :on-click #(rf/dispatch [::events/register!])
            :text     "Register"}]])])))

(defn register-confirmed
  [{:keys [selected-type typed-value]}]
  (let [type-text (->
                    (filter #(= selected-type (:type %)) register-select-options)
                    first
                    :text)]
   [:div
    [header-text "You're all set! Thanks for registering!"]
    [:p
     {:class "text-center"}
     (str "You've successfully registered with " type-text ": " typed-value)]
    [button
     {:on-click #(rf/dispatch [::events/initialize-db])
      :text     "Go back to register form"}]]))

(defn main
  []
  (let [registered?   (rf/subscribe [::subs/registered?])
        selected-type (rf/subscribe [::subs/form :selected-type])
        typed-value   (rf/subscribe [::subs/form :input-value])]
    (fn []
      (let [props {:selected-type @selected-type
                   :typed-value   @typed-value}]
        [:div
         {:class "m-auto max-w-screen-sm flex flex-col justify-center py-12 overflow-hidden gb-white sm:px-6 lg:px-8"}
         (if @registered?
           [register-confirmed props]
           [register-form props])]))))
