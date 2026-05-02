(ns advent-of-code-2025.day4.day4
  (:require [advent-of-code-2025.util.graph :as g]))

(def test-input "./src/advent_of_code_2025/day4/test-input.txt")
(def real-input "./src/advent_of_code_2025/day4/input.txt")

(def test-graph
  (g/parse-graph test-input))

(defn grab-neighbors [graph coord]
  (let [neighbor-indices (g/find-neighbor-indices coord)
        neighbor-indices-in-graph (filter #(g/is-in-graph? graph %) neighbor-indices)]
    (map #(g/get-graph-value graph %)) neighbor-indices-in-graph))

(defn valid-neighbors? [graph coord]
  (let [neighbor-indices (g/find-neighbor-indices coord)
        neighbor-indices-in-graph (filter #(g/is-in-graph? graph %) neighbor-indices)]
    (> 4 (count (filter #(= "@" %) (map #(g/get-graph-value graph %) neighbor-indices-in-graph))))))

(g/find-neighbor-indices [3 2])
(valid-neighbors? test-graph [1 0])

(valid-neighbors? test-graph [1 3])
(grab-neighbors test-graph [1 3])
(defn accessible-by-forklift? [graph [x y]]
  (and (= "@" (g/get-graph-value graph [x y]))
       (valid-neighbors? graph [x y])))

(defn grab-indices [graph]
  (for [x (range (count (first graph)))
        y (range (count graph))]
    [x y]))


(grab-indices test-graph)

test-graph
(defn part1 [graph]
  (filter #(accessible-by-forklift? graph %) (grab-indices graph)))

(map #(g/get-graph-value test-graph %) (grab-neighbors test-graph [1 3]))

(part1 test-graph)

(part1 (g/parse-graph real-input))
;; figure out the bug here!


