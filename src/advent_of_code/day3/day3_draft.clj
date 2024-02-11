(ns advent-of-code.day3.day3-draft
  (:require [clojure.set :as set]
            [clojure.string :as str]))

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

(defn is-in-graph?
  "docstring"
  [[x y] graph]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))


(defn is-digit?
  "Check if the given character represents a digit."
  [char]
  (try
    (Integer/parseInt (str char))
    true
    (catch NumberFormatException _
      false)))

(is-digit? "9")

(defn is-symbol?
  "docstring"
  [char]
  (not (or (is-digit? char) (= "." char))))

(defn is-valid-number
  "docstring"
  [index current-number y graph]
  (let [number-indices (map #(+ index %) (range (count current-number)))
        all-neighbor-indices (set (apply concat (map #(find-neighbor-indices % y) number-indices)))]
    (some is-symbol? (map (fn [[x y]] (subs (nth graph y) x (inc x))) (filter (fn [[x y]] (is-in-graph? [x y] graph))  all-neighbor-indices)))))

(defn find-valid-nums
  "docstring"
  [row, y, graph]
  (loop [index 0
         remaining row
         current-number ""
         nums []]
    (cond
      (and (empty? remaining) (seq current-number))
      (conj nums (Integer/parseInt current-number))
      (empty? remaining)
      nums
      (and (not (is-digit? (first remaining))) (not (empty? current-number)))
      (if (is-valid-number index current-number y graph)
        (recur (inc index) (rest remaining) "" (conj nums (Integer/parseInt current-number)))
        (recur (inc index) (rest remaining) "" nums))
      (is-digit? (first remaining))
      (recur (inc index) (rest remaining) (str current-number (first remaining)) nums)
      :else
      (recur (inc index) (rest remaining) current-number nums))))

;make a master set of neighbor indices

(defn split-string-into-list [s]
  (map str (seq s)))

(split-string-into-list "467..114..")

(defn sum-of-parts
  "docstring"
  [graph]
  (apply + (flatten (map-indexed (fn [index row] (find-valid-nums row index graph)) graph))))

(def graph (str/split "467..114..\n...*......\n..35..633.\n......#...\n617*......\n.....+.58.\n..592.....\n......755.\n...$.*....\n.664.598.." #"\n"))

(find-valid-nums (first graph) 0 graph)

;(is-in-graph? [2 -1] graph)


(sum-of-parts graph)

(is-valid-number 0 "467" 0 graph)

;should have solved this with tdd