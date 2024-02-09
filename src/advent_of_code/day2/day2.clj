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

(defn parse-number-after-game [s]
  (if-let [[_ num] (re-find #"Game\s+(\d+)" s)]
    (Integer/parseInt num)
    nil))

(defn valid-blue?
  "docstring"
  [num-blue]
  (>= 14 num-blue))

(defn valid-green?
  "docstring"
  [num-green]
  (if (nil? num-green)
    true
    (>= 13 num-green)))

(defn calc-score
  "docstring"
  [game]
  (if (and (valid-blue? (parse-number-before-blue game)) (valid-green? (parse-number-before-green game)))
    (parse-number-after-game game)
    0)
  )

(parse-number-before-blue "Game 1: 1 blue")


(defn calculate-sum-of-ids
  "docstring"
  [games-list]
  (calc-score (first games-list)))