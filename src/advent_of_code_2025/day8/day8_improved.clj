(ns advent-of-code-2025.day8.day8-improved 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

(defn sqrd [x]
  (Math/pow x 2))

(defn distance [[x1 y1 z1] [x2 y2 z2]]
  (Math/sqrt
   (+ (sqrd (- x1 x2)) (sqrd (- y1 y2)) (sqrd (- z1 z2)))))

(def test-input "./src/advent_of_code_2025/day8/test_input.txt")
(def input "./src/advent_of_code_2025/day8/input.txt")

(defn parse-points [input]
  (->> (slurp input)
       (str/split-lines)
       (map #(str/split % #","))
       (map (partial mapv parse-long))))

(parse-points test-input)

(defn sorted-connections [points]
  (sort-by first (for [p1 points ; it looks like sort automatically compares by first element
                       p2 points
                       :when (not= p1 p2)]
                   [(distance p1 p2) p1 p2])))

(take-nth 2 (sorted-connections (parse-points test-input)))

(defn create-circuits [points]
  (->> points
       (map hash-set)
       set))

(def test-circuits
  (->> test-input
       parse-points
       create-circuits))

(defn- find-circuit [circuits point]
  (first (filter #(contains? % point) circuits)))

(find-circuit test-circuits [431 825 988])

(defn- update-circuits [circuits [p1 p2]]
  (let [circuit1 (find-circuit circuits p1)
        circuit2 (find-circuit circuits p2)]
    (if (= circuit1 circuit2)
      circuits
      (conj (disj circuits circuit1 circuit2) (set/union circuit1 circuit2)))))

(defn build-final-circuits [points num-connections]
  (let [circuits (create-circuits points)
        connections-to-process (->> (sorted-connections points)
                                    (take-nth 2)
                                    (map rest)
                                    (take num-connections))]
    (reduce update-circuits circuits connections-to-process)))



(defn part1 [input num-connections]
  (let [points (parse-points input)
        final-circuits (build-final-circuits points num-connections)]
    (->> final-circuits
         (map count)
         (sort >)
         (take 3)
         (reduce *))))

(comment
  (part1 test-input 10)
  (part1 input 1000))

; If calculating distances is already n^2, maybe making the connection
; algorithm n^2 isn't that bad too. I was worried about that, but the premature
; optimization wasn't worth it

(defn build-until-fully-merged [points]
  (let [circuits (create-circuits points)
        connections-to-process (->> (sorted-connections points)
                                    (take-nth 2)
                                    (map rest))]
    (loop [circuits' circuits
           remaining-connections connections-to-process
           processed-connections []]
      (if (= 1 (count circuits'))
        processed-connections
        (recur (update-circuits circuits' (first remaining-connections))
               (rest remaining-connections)
               (conj processed-connections (first remaining-connections)))))))

(defn- calc-pt2-score [[[x1 _ _] [x2 _ _]]]
  (* x1 x2))

(defn part2 [input]
  (let [points (parse-points input)
        processed-connections (build-until-fully-merged points)]
    (->> processed-connections
         last
         calc-pt2-score)))

(part2 test-input)
(part2 input)
