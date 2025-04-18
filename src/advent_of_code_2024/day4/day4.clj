(ns advent-of-code-2024.day4.day4
  (:require
   [clojure.string :as str]))

(defrecord Point [x y])

(def real-input
  "Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day4/day4_input.txt")

(def test-input
  "Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day4/day4_test_input.txt")

(defn get-graph-value
  [graph p]
  (nth (nth graph (:y p)) (:x p)))

(defn is-in-graph?
  [graph p]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length (:x p)) (> graph-height (:y p)) (not (neg-int? (:x p))) (not (neg-int? (:y p))))))

(defn find-neighbors
  [p]
  (let [x (:x p)
        y (:y p)]
    [(Point. (- x 1) (- y 1))
     (Point. (- x 1) y)
     (Point. (- x 1) (+ y 1))
     (Point. x (- y 1))
     (Point. x (+ y 1))
     (Point. (+ x 1) (- y 1))
     (Point. (+ x 1) y)
     (Point. (+ x 1) (+ y 1))]))

(defn find-valid-neighbors [graph, p]
  (filter #(is-in-graph? graph %) (find-neighbors p)))

(defn parse-graph [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #""))))

(def test-graph (parse-graph test-input))

(defn get-all-points [graph]
  (for [x (range (count (first graph)))
        y (range (count graph))]
    (Point. x y)))

(defn is-xmas-prefix? [s]
  (= (subs "XMAS" 0 (count s)) s))

; This was never used, as we don't need a true search for this problem
(defn find-xmas-paths [graph p]
  (loop [tasks [{:current-point p, :current-string (get-graph-value graph p), :current-path [p]}]
         xmas-paths []]
    (if (empty? tasks)
      xmas-paths
      (let [current-task (first tasks)
            current-point (:current-point current-task)
            current-string (:current-string current-task)
            current-path (:current-path current-task)
            valid-neighbors (find-valid-neighbors graph current-point)]
        (cond
          (= current-string "XMAS")
          (recur (rest tasks) (conj xmas-paths current-path))
          (is-xmas-prefix? current-string)
          (recur (concat (rest tasks)
                         (map (fn [neighbor] {:current-point neighbor :current-string (str current-string (get-graph-value graph neighbor)), :current-path (conj current-path neighbor)}) valid-neighbors)) xmas-paths)
          :else
          (recur (rest tasks) xmas-paths))))))

(def directions
  [{:dx 0  :dy -1}  ;; up
   {:dx 0  :dy 1}   ;; down
   {:dx -1 :dy 0}   ;; left
   {:dx 1  :dy 0}   ;; right
   {:dx -1 :dy -1}  ;; up-left
   {:dx 1  :dy -1}  ;; up-right
   {:dx -1 :dy 1}   ;; down-left
   {:dx 1  :dy 1}]) ;; down-right

(defn generate-4point-lines [^Point point]
  (for [{:keys [dx dy]} directions]
    (mapv (fn [step]
            (->Point (+ (.x point) (* dx step))
                     (+ (.y point) (* dy step))))
          (range 4))))

(generate-4point-lines (Point. 0 0))

(defn is-equal-to-xmas? [graph line]
  (if (every? #(is-in-graph? graph %) line)
    (= (apply str (map #(get-graph-value graph %) line)) "XMAS")
    false))

(defn find-num-basic-xmas-paths [graph p]
  (let [lines (generate-4point-lines p)]
    (reduce + (map (fn [line] (if (is-equal-to-xmas? graph line)
                                1
                                0))
                   lines))))

(defn part1 [graph]
  (let [points (get-all-points graph)]
    (reduce + (map #(find-num-basic-xmas-paths graph %) points))))

(part1 test-graph)

(part1 (parse-graph real-input))

;; Correct for part1

(is-equal-to-xmas? test-graph [(Point. 0 0) (Point. 0 1) (Point. 0 2) (Point. 0 3)])

(defn find-all-xmas-paths [graph]
  (let [points (get-all-points graph)]
    (for [p points
          xmas-paths (find-xmas-paths graph p)]
      xmas-paths)))

(def my-point (Point. 0 0))
(get-graph-value (parse-graph test-input) my-point)

(find-neighbors my-point)
(find-valid-neighbors test-graph my-point)

(find-all-xmas-paths test-graph)

(find-xmas-paths test-graph (Point. 9 9))

(find-valid-neighbors test-graph (Point. 9 9))
