(ns advent-of-code.day2.part2
  (:require [advent-of-code.day2.day2 :as day2]
            [clojure.string :as str]))

(defn parse-number-before-blue [s]
  (if-let [[_ num] (re-find #"(\d+)\s+blue" s)]
    (Integer/parseInt num)
    0))

(defn parse-number-before-red [s]
  (if-let [[_ num] (re-find #"(\d+)\s+red" s)]
    (Integer/parseInt num)
    0))

(defn parse-number-before-green [s]
  (if-let [[_ num] (re-find #"(\d+)\s+green" s)]
    (Integer/parseInt num)
    0))

(defn calc-score
  "docstring"
  [game]
  (let [rounds (str/split game #";")
        max-blue (apply max (map parse-number-before-blue rounds))
        max-green (apply max (map parse-number-before-green rounds))
        max-red (apply max (map parse-number-before-red rounds))]
    (* max-blue max-green max-red)))

(defn calc-sum-of-products
  "docstring"
  [games]
  (apply + (map calc-score games)))

(-> "./src/advent_of_code/day2/game-values.txt"
    slurp
    (str/split #"\n")
    calc-sum-of-products)
