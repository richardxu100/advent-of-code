(ns day2
  (:require
   [clojure.string :as str]))

(def str_lines (str/split-lines (slurp "Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day2/day2_input.txt")))

(def str_line_elements (map (fn [element] (str/split element #" ")) str_lines))

str_line_elements
(def list_of_ints (map (fn [line] (map (fn [string] (Integer/parseInt string)) line)) str_line_elements))

list_of_ints

(defn all-increasing? [line]
  (= (sort line) line))

(defn all-decreasing? [line]
  (= (sort > line) line))

(all-increasing? [1 3 3 5 12])
(all-decreasing? [9 7 5 5 2 1])

(defn in-safe-range? [n]
  (contains? (set '(1 2 3)) n))

(defn valid_adj_levels? [line]
  (loop [remaining_els (rest line)
         current (first line)]
    (cond
      (empty? remaining_els)
      true
      (in-safe-range? (abs (- current (first remaining_els))))
      (recur (rest remaining_els) (first remaining_els))
      :else
      false)))

(defn is-line-safe? [line]
  (and (or (all-increasing? line) (all-decreasing? line))
       (valid_adj_levels? line)))

(defn safe_lines [lines]
  (for [line lines
        :when (is-line-safe? line)]
    line))

(count (safe_lines list_of_ints))

