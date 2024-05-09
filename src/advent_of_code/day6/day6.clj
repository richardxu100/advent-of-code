(ns advent-of-code.day6.day6)

(defn calc-distance [hold-time total-time]
  (* hold-time (- total-time hold-time)))

(defn margin-of-error
  "docstring"
  [races]
  (let [only-race (first races)
        total-time (:time only-race)
        record (:record only-race)]
    (count (for [hold-time (range total-time) :when (> (calc-distance hold-time total-time) record)]
       hold-time))))