(ns advent-of-code-2025.day4.day4
  (:require [advent-of-code-2025.util.graph :as g]))

(def test-input "./src/advent_of_code_2025/day4/test-input.txt")
(def real-input "./src/advent_of_code_2025/day4/input.txt")

(def test-graph
  (g/parse-graph test-input))

(defn toilet-paper? [s]
  (= "@" s))

(defn- grab-neighbors [graph point]
  (map #(g/get-val graph %)) (g/find-neighbor-indices graph point))

(defn accessible-by-forklift? [graph point]
  (->> (g/find-neighbor-indices graph point)
       (map #(g/get-val graph %))
       (filter toilet-paper?)
       count
       (> 4)))

(defn grab-indices [graph]
  (for [x (range (count (first graph)))
        y (range (count graph))]
    [x y]))

(comment
  (grab-indices test-graph)
  test-graph)

(defn part1 [graph]
  (->> (grab-indices graph)
       (filter #(and (toilet-paper? (g/get-val graph %))
                     (accessible-by-forklift? graph %)))
       count))

(comment
  (map #(g/get-val test-graph %) (grab-neighbors test-graph [1 3]))
  (part1 test-graph))

(part1 (g/parse-graph real-input))


