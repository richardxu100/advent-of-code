(ns advent-of-code.day2.day2)

(defn parse-number-before-blue [s]
  (if-let [[_ num] (re-find #"(\d+)\s+blue" s)]
    (Integer/parseInt num)
    nil))

(defn parse-number-before-red [s]
  (if-let [[_ num] (re-find #"(\d+)\s+red" s)]
    (Integer/parseInt num)
    nil))

(defn parse-number-before-green [s]
  (if-let [[_ num] (re-find #"(\d+)\s+green" s)]
    (Integer/parseInt num)
    nil))

(defn calc-score
  "docstring"
  [game]
  )



(defn valid-blue?
  "docstring"
  [num-blue]
  (>= num-blue 14))

(defn calculate-sum-of-ids
  "docstring"
  [games-list]
  (reduce + (map calc-score games-list)))