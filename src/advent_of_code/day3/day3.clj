(ns advent-of-code.day3.day3
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

(defn get-current-num
  "docstring"
  [x row]
  (apply str (take-while is-digit? (drop x row)))
)

(defn is-left-neighbor-digit?
  "docstring"
  [x row]
  (if (zero? x)
    false
    (is-digit? (nth row (dec x)))))

(defn another-find-valid-nums
  "docstring"
  [graph row y]
  (for [x (range (count row))
        :let [current-num (get-current-num x row)]
        :when (and (not (is-left-neighbor-digit? x row)) (not (empty? current-num)) (is-valid-num current-num x y graph))]
    (Integer/parseInt current-num)
    ))

(defn sum-of-parts
  "docstring"
  [graph]
  (apply + (map-indexed (fn [y row] (apply + (another-find-valid-nums graph row y)) ) graph)))

(let [graph (->
             "./src/advent_of_code/day3/graph-input.txt"
             (slurp)
             (str/split #"\n"))]
  (->> graph
       (map (fn [row] (str/split row #"")))
       sum-of-parts))
