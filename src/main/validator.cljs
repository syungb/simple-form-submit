(ns main.validator
  (:require
    [clojure.string]))

(defn valid-email-or-tel?
  "Given a type and a non-blank string value, returns boolean to validate
  if the value is provided with valid format based on a regex matching..

  NOTE: limited phone number formats are supported at this point.
        i.e. +10000000000, 000-000-0000"
  [type value]
  (let [pattern
        (case type
          "email" #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
          "tel" #"^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$"
          #"")]
    (and
      (not (clojure.string/blank? value))
      (re-matches pattern value))))
