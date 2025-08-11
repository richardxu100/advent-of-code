(ns advent-of-code-2024.day6.day6
  (:require
    [clojure.string :as str]))

; find the starting position
; need a visited set
; need to know when out of bounds
; Need to know the current direction
; The directions go from north, east, south, west, north
; Navigation

(def test-input
  "/Users/Richard/Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day6/test_input.txt")

(def real-input
  "/Users/Richard/Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day6/input.txt")


(slurp test-input)

(defn parse-graph [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #""))))

(def test-graph
  (parse-graph test-input))

(defn find-starting-pos [graph]
  (first
    (for [x (range (count (first graph)))
          y (range (count graph))
          :when (= "^" (nth (nth graph y) x))]
      {:x x :y y})))

(find-starting-pos test-graph)


(defn next-dir
  [dir]
  (case dir
    :N :E
    :E :S
    :S :W
    :W :N))

(defn smaller-y [a b]
  (min (:y a) (:y b)))

(defn bigger-y [a b]
  (max (:y a) (:y b)))

(defn smaller-x [a b]
  (min (:x a) (:x b)))

(defn bigger-x [a b]
  (max (:x a) (:x b)))

(comment
  (smaller-y {:y 10} {:y 2}))

; write this better
(defn calc-updated-visited [current-pos next-pos visited]
  (if (= (:x current-pos) (:x next-pos))
    (into visited (for [y (range (smaller-y current-pos next-pos) (inc (bigger-y current-pos next-pos)))]
                    {:x (:x current-pos) :y y}))
    (into visited (for [x (range (smaller-x current-pos next-pos) (inc (bigger-x current-pos next-pos)))]
                    {:x x :y (:y current-pos)}))))

(comment
  (calc-updated-visited {:x 2 :y 6} {:x 6 :y 6} #{})
  (calc-updated-visited {:x 6 :y 15} {:x 6 :y 6} #{}))

(defn get-graph-value [graph index]
  (let [x (:x index)
        y (:y index)]
    (nth (nth graph y) x)))

test-graph

(defn is-in-graph? [graph point]
  (and (< (:y point) (count graph))
       (< (:x point) (count (first graph)))))

(is-in-graph? [[0 1 2 3 4] [0 1 2 3 4]] {:x 5 :y 1})

(defn calc-path-indices [pos dir graph])

(defn find-next-pos [pos dir]
  (let [x (:x pos)
        y (:y pos)]
    (case dir
      :N {:x x :y (dec y)}
      :S {:x x :y (inc y)}
      :W {:x (dec x) :y y}
      :E {:x (inc x) :y y})))

(comment
  (find-next-pos {:x 2 :y 2} :E))

(defn find-next-destination [pos dir graph]
  (loop [current-pos pos]
    (let [next-pos (find-next-pos current-pos dir)]
      (if (not (is-in-graph? graph next-pos))
        next-pos
        (if (= "#" (get-graph-value graph next-pos))
          current-pos
          (recur next-pos))))))

(comment
  (find-next-destination {:x 1 :y 2} :S test-graph))


(defn calc-distinct-positions [graph]
  (let [starting-pos (find-starting-pos graph)]
    (loop [current-pos starting-pos
           current-dir :N
           visited #{}]
      (if (not (is-in-graph? graph current-pos))
        visited
        (let [next-destination (find-next-destination current-pos current-dir graph)]
          (recur next-destination (next-dir current-dir) (calc-updated-visited current-pos next-destination visited)))))))

(calc-distinct-positions test-graph)

(defn part1 [input]
  (let [graph (parse-graph input)
        distinct-positions (calc-distinct-positions graph)]
     (filter #(is-in-graph? graph %) distinct-positions))) ; remove the last visited place

(part1 test-input)
(part1 real-input)





