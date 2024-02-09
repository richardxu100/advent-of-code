(ns advent-of-code.day2.day2
  (:require [clojure.string :as str]))

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
  (if (nil? num-blue)
    true
    (>= 14 num-blue))
  )

(defn valid-green?
  "docstring"
  [num-green]
  (if (nil? num-green)
    true
    (>= 13 num-green)))

(defn valid-red?
  "docstring"
  [num-red]
  (if (nil? num-red)
    true
    (>= 12 num-red)))

(defn valid-round?
  "docstring"
  [round]
  (and (valid-blue? (parse-number-before-blue round)) (valid-green? (parse-number-before-green round)) (valid-red? (parse-number-before-red round)))
  )

(defn calc-score
  "docstring"
  [game]
  (if (every? valid-round? (str/split game #";"))
    (parse-number-after-game game)
    0)
  )

(every? valid-round? (str/split "Game 1: 1 blue 2 red; 100 red" #";"))

(parse-number-before-blue "Game 1: 1 blue")


(defn calculate-sum-of-ids
  "docstring"
  [games-list]
  (apply + (map calc-score games-list)))


(-> "./src/advent_of_code/day2/game-values.txt"
    slurp
    (str/split #"\n")
    calculate-sum-of-ids)
