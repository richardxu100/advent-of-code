(ns advent-of-code-2025.day4.day4
  (:require [advent-of-code-2025.util.graph :as g]))

(def test-input "./src/advent_of_code_2025/day4/test-input.txt")
(def real-input "./src/advent_of_code_2025/day4/input.txt")

(def test-graph
  (g/parse-graph test-input))

(defn toilet-paper? [s]
  (= "@" s))

(defn grab-neighbors [graph coord]
  (let [neighbor-indices (g/find-neighbor-indices coord)
        neighbor-indices-in-graph (filter #(g/is-in-graph? graph %) neighbor-indices)]
    (map #(g/get-graph-value graph %)) neighbor-indices-in-graph))

(defn accessible-by-forklift? [graph coord]
  (let [neighbor-indices (g/find-neighbor-indices coord)
        neighbor-indices-in-graph (filter #(g/is-in-graph? graph %) neighbor-indices)]
    (> 4 (count (filter toilet-paper? (map #(g/get-graph-value graph %) neighbor-indices-in-graph))))))

(defn grab-indices [graph]
  (for [x (range (count (first graph)))
        y (range (count graph))]
    [x y]))

(comment
  (grab-indices test-graph)
  test-graph)
(defn part1 [graph]
  (count (filter #(and (toilet-paper? (g/get-graph-value graph %)) (accessible-by-forklift? graph %)) (grab-indices graph))))

(comment
  (map #(g/get-graph-value test-graph %) (grab-neighbors test-graph [1 3]))
  (part1 test-graph))

(part1 (g/parse-graph real-input))


