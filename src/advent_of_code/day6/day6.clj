(ns advent-of-code.day6.day6
  (:require [clojure.string :as str]))

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

(defn parse-input
  "docstring"
  []
  (let [lines (-> "./src/advent_of_code/day6/new-input.txt"
                  slurp
                  (str/split #"\n")
                  )]
    (->>
      lines
      (map #(str/split % #" "))
      (map #(map parse-long %))
      (map #(filterv some? %)))))

(defn construct-races-list
  "docstring"
  []
  (let [input (parse-input)
        [times records] input
        pairs (map vector times records)]
    (for [[time record] pairs] {:time time :record record})))

(construct-races-list)

(margin-of-error (construct-races-list))