(ns advent-of-code-2025.day7.day7 
  (:require
    [advent-of-code-2025.util.graph :as g]))

(def test-input "./src/advent_of_code_2025/day7/test_input.txt")
(def input "./src/advent_of_code_2025/day7/input.txt")

(def test-graph
  (g/parse-graph test-input))

(defn entrance? [s] (= "S" s))
(defn beam? [s] (= "|" s))
(defn splitter? [s] (= "^" s))

(defn add-beams [board indices]
  (reduce #(assoc-in %1 %2 "|") board (map reverse indices))) ;; okay to explain this, we're passing the board and indices to reduce, which is why we have %1 and %2 here

(defn find-indices [coll pred]
  (->> coll
       (map-indexed vector)
       (filter #(pred (second %)))
       (map first)))

(defn first-turn [board]
  (let [entrance-index (first (find-indices (first board) entrance?))]
    (add-beams board [[entrance-index 1]])))

(first-turn test-graph)

(defn extend-beam-to-next-row [board [x y]]
  (if (splitter? (g/get-val board [x (inc y)]))
    (add-beams board [[(dec x) (inc y)] [(inc x) (inc y)]])
    (add-beams board [[x (inc y)]])))

(defn update-next-row [board index]
  (let [current-row (nth board index)
        beam-indices (find-indices current-row beam?)
        beam-coords (map #(vector % index) beam-indices)]
    (reduce extend-beam-to-next-row board beam-coords))) ; I like using reduce to apply recursive updates

(comment
  (map #(vector % 4) [3 1 5 2])
  (update-next-row (first-turn test-graph) 1))

(defn update [board index]
  (condp = index
    0 (first-turn board)
    (dec (count board)) board
    (update-next-row board index)))

(defn game [input]
  (let [board (g/parse-graph input)]
    (reduce update board (range (count board))))) ; this is nicer than the loop recur of before!

(game test-input)

(defn num-splits [idx board]
  (let [splitter-indices (find-indices (nth board idx) splitter?)]
    (->> splitter-indices
         (filter #(beam? (g/get-val board [% (dec idx)]))) ;; see if a beam is directly above the splitter
         count)))

(defn part1 [input]
  (let [result (game input)]
    (reduce + (for [idx (range (count result))]
              (num-splits idx result)))))

(part1 test-input)

(comment
  (num-splits 15 test-graph)
  (count test-graph)
  (count (game test-input)))

(part1 input)

;;; Part 2
;; I think I should do a search algorithm, that adds to a master list of possible routes

(defn child-paths [[x y] graph]
  (if (entrance? (g/get-val graph [x y]))
    [[x 1]]
    (if (splitter? (g/get-val graph [x (inc y)]))
      [[(dec x) (inc y)] [(inc x) (inc y)]]
      [[x (inc y)]])))

;; I'm not going to de-duplicate the paths. I'm not sure if that's necessary yet
(defn calc-paths [graph]
  (let [entrance-index (first (find-indices (first graph) entrance?))
        entrance-node [entrance-index 0]]
    (loop [nodes [entrance-node]
           num-paths 0]
      (if (empty? nodes)
        num-paths
        (let [current-node (first nodes)]
          (if (= (second current-node) (dec (count graph))) ; if reached terminal area of graph
            (recur (rest nodes) (inc num-paths))
            (recur (into (rest nodes) (child-paths current-node graph)) num-paths)))))))

(calc-paths test-graph)

(g/get-val test-graph [14 15])
(count test-graph)

(defn part2 [input]
  (let [graph (g/parse-graph input)]
    (calc-paths graph)))

(part2 test-input)
