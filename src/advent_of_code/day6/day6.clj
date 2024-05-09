(ns advent-of-code.day6.day6)

(defn calc-distance [hold-time total-time]
  (* hold-time (- total-time hold-time)))

(defn calc-margin-of-error-for-race
  "docstring"
  [race]
  (let [total-time (:time race)
        record (:record race)]
    (count (for [hold-time (range total-time) :when (> (calc-distance hold-time total-time) record)]
             hold-time))))

(defn margin-of-error
  "docstring"
  [races]
  (apply * (map calc-margin-of-error-for-race races)))