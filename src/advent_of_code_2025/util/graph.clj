(ns advent-of-code-2025.util.graph
  (:require [clojure.string :as str]))

(defn parse-graph [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #""))
       vec))

(defn in-graph?
  [graph [x y]]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))

(defn get-val
  "Returns value of the point in the graph"
  [graph [x y]]
  (nth (nth graph y) x))

(defn- find-possible-neighbor-indices
  [[x y]]
  [[(- x 1) (- y 1)]
   [(- x 1) y]
   [(- x 1) (+ y 1)]
   [x (- y 1)]
   [x (+ y 1)]
   [(+ x 1) (- y 1)]
   [(+ x 1) y]
   [(+ x 1) (+ y 1)]])

(defn find-neighbor-indices
  [graph point]
  (let [possible-neighbor-indices (find-possible-neighbor-indices point)]
    (filter #(in-graph? graph %) possible-neighbor-indices)))

