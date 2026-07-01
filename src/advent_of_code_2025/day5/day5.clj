(ns advent-of-code-2025.day5.day5
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def test-input "./src/advent_of_code_2025/day5/test_input.txt")
(def real-input "./src/advent_of_code_2025/day5/input.txt")


(defn convert-to-range [s]
  (let [[l r] (str/split s #"-")]
    [(parse-long l) (parse-long r)]))

(defn parse-input [input]
  (let [lines (str/split-lines (slurp input))
        split-index (.indexOf lines "")]
    {:ranges (map convert-to-range (take split-index lines))
     :ids    (map parse-long (take-last (dec (- (count lines) split-index)) lines))}))

(parse-input test-input)

(defn in-range? [[l r] id]
  (and (<= l id) (>= r id)))

(defn in-any-range? [ranges id]
  (boolean (some #(in-range? % id) ranges)))

(defn part1 [input]
  (let [{ranges :ranges ids :ids} (parse-input input)]
    (count (filter (partial in-any-range? ranges) ids))))

(part1 real-input)

(defn overlaps? [r1 r2]
  (let [[l1 r1] r1
        [l2 r2] r2]
    (or (>= r1 l2 l1) (>= r2 l1 l2))))

(comment
  (overlaps? [5 10] [12 23])
  (overlaps? [5 10] [8 23])
  (overlaps? [8 23] [5 10])
  (overlaps? [10 23] [5 10]))

(defn find-overlapping-range [range remaining-ranges]
  (loop [remaining-ranges' remaining-ranges]
    (cond
      (empty? remaining-ranges')
      nil
      (overlaps? (:range range) (:range (first remaining-ranges')))
      (first remaining-ranges')
      :else
      (recur (rest remaining-ranges')))))

(defn merge-range [indexed-r1 indexed-r2]
  (let [r1 (:range indexed-r1)
        r2 (:range indexed-r2)
        min-x (min (first r1) (first r2))
        max-y (max (second r1) (second r2))]
    {:range [min-x max-y] :id (:id indexed-r1)}))


(defn remove-id [ranges id]
  (filter #(not (= (:id %) id)) ranges))

(defn consolidate
  "Merge ranges until there are no overlaps"
  [indexed-ranges]
  (loop [current-range (first indexed-ranges)
         remaining-ranges (rest indexed-ranges)
         consolidated-ranges []]
    (cond
      (empty? remaining-ranges)
      (if (empty? current-range)
        consolidated-ranges
        (cons current-range consolidated-ranges))
      :else
      (let [overlapping-range (find-overlapping-range current-range remaining-ranges)]
        (if (nil? overlapping-range)
          (recur (first remaining-ranges) (rest remaining-ranges) (cons current-range consolidated-ranges))
          (let [consolidated-range (merge-range current-range overlapping-range)
                remaining-ranges' (remove-id remaining-ranges (:id overlapping-range))]
            (recur consolidated-range remaining-ranges' consolidated-ranges)))))))


(defn convert-to-indexed-range [index s]
  (let [[l r] (str/split s #"-")]
    {:id index :range [(parse-long l) (parse-long r)]}))

(defn parse-ranges-part-2 [input]
  (let [lines (str/split-lines (slurp input))
        split-index (.indexOf lines "")]
    (map-indexed convert-to-indexed-range (take split-index lines))))

(comment
  (parse-ranges-part-2 test-input)
  (consolidate (parse-ranges-part-2 test-input))
  (consolidate (parse-ranges-part-2 real-input)))

(defn get-range-size [{r :range}]
  (inc (- (second r) (first r))))

(defn part2 [input]
  (let [indexed-ranges (parse-ranges-part-2 input)
        non-overlapping-ranges (consolidate indexed-ranges)]
    (reduce + (map get-range-size non-overlapping-ranges))))

(part2 test-input)
(part2 real-input)
