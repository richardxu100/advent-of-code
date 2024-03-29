(ns advent-of-code.day3.part2
  (:require [clojure.string :as str]))

(defn is-digit?
  "Check if the given character represents a digit."
  [char]
  (try
    (Integer/parseInt (str char))
    true
    (catch NumberFormatException _
      false)))

(defn is-symbol?
  "docstring"
  [char]
  (not (or (is-digit? char) (= "." char))))

(defn is-in-graph?
  "docstring"
  [graph [x y]]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))

(defn get-graph-value
  "docstring"
  [graph [x y]]
  (nth (nth graph y) x))

(defn find-neighbor-indices
  "docstring"
  [x y]
  [[(- x 1) (- y 1)]
   [(- x 1) y]
   [(- x 1) (+ y 1)]
   [x (- y 1)]
   [x (+ y 1)]
   [(+ x 1) (- y 1)]
   [(+ x 1) y]
   [(+ x 1) (+ y 1)]])

(defn is-valid-num
  "docstring"
  [current-num current-num-index y graph]
  (->> (range current-num-index (+ current-num-index (count current-num)))
       (map #(find-neighbor-indices % y))
       (apply concat)
       (set)
       (filter #(is-in-graph? graph %))
       (map #(get-graph-value graph %))
       (some is-symbol?)))

(defn find-valid-gears
  "docstring"
  [graph row y]
  (loop [index 0
         remaining row
         current-num ""
         current-num-index nil
         valid-nums []]
    (cond
      (empty? remaining)
      (if (empty? current-num)
        (apply + valid-nums)
        (if (is-valid-num current-num current-num-index y graph)
          (let [all-valid-nums (conj valid-nums (Integer/parseInt current-num))] (apply + all-valid-nums))
          (apply + valid-nums)))
      (is-digit? (first remaining))
      (recur (inc index) (rest remaining) (str current-num (first remaining)) (if (nil? current-num-index)
                                                                                index
                                                                                current-num-index) valid-nums)
      (seq current-num)
      (recur (inc index) (rest remaining) "" nil (if (is-valid-num current-num current-num-index y graph)
                                                   (conj valid-nums (Integer/parseInt current-num))
                                                   valid-nums))
      :else
      (recur (inc index) (rest remaining) current-num current-num-index valid-nums))))

(defn sum-of-parts
  "docstring"
  [graph]
  (apply + (map-indexed (fn [y row] (find-valid-gears graph row y)) graph)))

(let [graph (->
             "./src/advent_of_code/day3/graph-input.txt"
             (slurp)
             (str/split #"\n"))]
  (->> graph
       (map (fn [row] (str/split row #"")))
       sum-of-parts))

