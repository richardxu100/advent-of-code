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

(defn first-turn [board]
  (let [entrance-index (->> (first board)
                        (map-indexed vector)
                        (filter #(entrance? (second %)))
                        first
                        first)]
    (add-beams board [[entrance-index 1]])))

(first-turn test-graph)

(defn extend-beam-to-next-row [board [x y]]
  (if (splitter? (g/get-val board [x (inc y)]))
    (add-beams board [[(dec x) (inc y)] [(inc x) (inc y)]])
    (add-beams board [[x (inc y)]])))

(defn update-next-row [board index]
  (let [current-row (nth board index)
        beam-indices (->> current-row
                          (map-indexed vector)
                          (filter #(beam? (second %)))
                          (map first))
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
  (let [splitter-indices (->> (nth board idx)
                              (map-indexed vector)
                              (filter #(splitter? (second %)))
                              (map first))]
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
