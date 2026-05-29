(ns advent-of-code-2025.day5.day5
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def test-input "./src/advent_of_code_2025/day5/test_input.txt")
(def real-input "./src/advent_of_code_2025/day5/input.txt")

(comment
  (defn old-convert-to-range [s]
    (let [[l r] (str/split s #"-")]
      (range (parse-long l) (inc (parse-long r)))))

  (old-convert-to-range "3-6")

  (defn old-parse-input [input]
    (let [lines (str/split-lines (slurp input))
          split-index (.indexOf lines "")]
      {:ranges (map old-convert-to-range (take split-index lines))
       :ids    (map parse-long (take-last (dec (- (count lines) split-index)) lines))}))

  (old-parse-input test-input)

  (defn destructure-practice [{r :ranges i :ids}]
    (println r i))

  (destructure-practice (old-parse-input test-input))

  (concat #{1 2 3} (range 5 12))

  (defn naive-part1 [input]
    (let [{ranges :ranges ids :ids} (old-parse-input input)
          valid-id-set (reduce #(set/union %1 (set %2)) #{} ranges)]
      (count (filter #(contains? valid-id-set %) ids))))

  (naive-part1 test-input))

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

(< 2 10)

(> 10 8 5)
(> 23 5 8)
()

(defn overlaps? [r1 r2]
  (let [[l1 r1] r1
        [l2 r2] r2]
    (or (>= r1 l2 l1) (>= r2 l1 l2))))

(overlaps? [5 10] [12 23])
(overlaps? [5 10] [8 23])
(overlaps? [8 23] [5 10])
(overlaps? [10 23] [5 10])

(defn cannot-consolidate? [ranges]
  (loop [current-range (first ranges)
         remaining-ranges (rest ranges)
         remaining-ranges' (rest ranges)]
    (cond
      (and (empty? remaining-ranges) (empty? remaining-ranges'))
      true
      (empty? remaining-ranges)
      (recur (first remaining-ranges') (rest remaining-ranges') (rest remaining-ranges'))
      (overlaps? current-range (first remaining-ranges))
      false
      :else
      (recur current-range (rest remaining-ranges) remaining-ranges'))))

(cannot-consolidate? [[1 3] [3 9]])

(defn consolidate [ranges]
  )

(defn handle-add-range [non-overlapping-ranges range]
  (loop [ranges (conj non-overlapping-ranges range)]
    (if (cannot-consolidate? ranges)
      ranges
      (recur (consolidate ranges)))))

(defn part2 [input]
  (let [{ranges :ranges} parse-input
        non-overlapping-ranges (reduce (partial handle-add-range []) ranges)]))

