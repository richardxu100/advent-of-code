(ns advent-of-code-2025.util.graph
  (:require [clojure.string :as str]))

(defn parse-graph [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #""))))

(defn find-neighbor-indices
  [[x y]]
  [[(- x 1) (- y 1)]
   [(- x 1) y]
   [(- x 1) (+ y 1)]
   [x (- y 1)]
   [x (+ y 1)]
   [(+ x 1) (- y 1)]
   [(+ x 1) y]
   [(+ x 1) (+ y 1)]])

(defn in-graph?
  [graph [x y]]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))

(defn get-val
  "Returns value of the point in the graph"
  [graph [x y]]
  (nth (nth graph y) x))

