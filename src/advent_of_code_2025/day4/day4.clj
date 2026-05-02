(ns advent-of-code-2025.day4.day4
  (:require [advent-of-code-2025.util.graph :as g]))

(def test-input "./src/advent_of_code_2025/day4/test-input.txt")
(def real-input "./src/advent_of_code_2025/day4/input.txt")

(def test-graph
  (g/parse-graph test-input))

(def real-graph
  (g/parse-graph real-input))

(defn toilet-paper? [s]
  (= "@" s))

(defn- grab-neighbors [graph point]
  (map (partial g/get-val graph)) (g/find-neighbor-indices graph point))

(defn accessible-by-forklift? [graph point]
  (->> (g/find-neighbor-indices graph point)
       (map (partial g/get-val graph))
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

(defn find-forkliftable-indices [graph]
  (->> (grab-indices graph)
       (filter #(and (toilet-paper? (g/get-val graph %))
                     (accessible-by-forklift? graph %)))))


(defn part1 [graph]
  (->> (grab-indices graph)
       (filter #(and (toilet-paper? (g/get-val graph %))
                     (accessible-by-forklift? graph %)))
       count))

(comment
  (map #(g/get-val test-graph %) (grab-neighbors test-graph [1 3]))
  (part1 test-graph))

(part1 real-graph)

(comment
  (def simple-graph [["@" "*"]
                     ["*" "@"]])
  (assoc-in simple-graph [0 0] "o"))

(defn remove-toilet-paper [graph points]
  (reduce #(assoc-in %1 %2 '.') graph (map reverse points)))

(comment
  (reverse [0 1])
  test-graph
  (reduce + 5 [1 2 3])
  (assoc-in test-graph [0 1] ".")
  (assoc-in test-graph [2 0] ".")
  (remove-toilet-paper test-graph [[0 1] [1 1]]))

(comment
  (remove-toilet-paper simple-graph [[0 0] [1 1]]))

(defn part2 [graph]
  (loop [graph' graph
         forkliftable-indices (find-forkliftable-indices graph)
         removed-count 0]
    (if (empty? forkliftable-indices)
      removed-count
      (let [updated-graph (remove-toilet-paper graph' forkliftable-indices)]
        (recur updated-graph
               (find-forkliftable-indices updated-graph)
               (+ removed-count (count forkliftable-indices)))))))

(comment
  (part2 simple-graph)
  (part2 test-graph)
  (remove-toilet-paper test-graph [[2 0]])
  (remove-toilet-paper test-graph [[0 1]]))

(part2 real-graph)
