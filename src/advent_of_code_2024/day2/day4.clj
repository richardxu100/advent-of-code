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

; Part 2

(defn generate_line_permutations [line]
  "Generates all variations of a line, minus an element"
  (loop [permutations []
         current_index 0]
    (if (= current_index (count line))
      permutations
      (recur (conj permutations (vec (keep-indexed #(when (not= %1 current_index) %2) line))) (inc current_index)))))

(defn generate_line_permutations_comprehension [line]
  (for [i (range (count line))]
    (vec (keep-indexed #(when (not= %1 i) %2) line))))

(generate_line_permutations [1 2 3 4 5])
(generate_line_permutations_comprehension [1 2 3 4 5])
(generate_line_permutations [25 26 29 30 32 35 37 35])

(defn new_safe_lines [lines]
  (for [line lines
        :when (or (is-line-safe? line) (some is-line-safe? (generate_line_permutations line)))]
    line))

(count (new_safe_lines list_of_ints))

; Complete day 2 :- j


