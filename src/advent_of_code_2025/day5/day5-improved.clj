(ns advent-of-code-2025.day5.day5-improved (:require [clojure.string :as str]
                                                     ))

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


(defn simpler-merge-range [r1 r2]
  (let [min-x (min (first r1) (first r2))
        max-y (max (second r1) (second r2))]
    [min-x max-y]))


(defn- sort-ranges [ranges]
  (sort-by first ranges))

(sort-ranges [{:range [5 10]} {:range [2 41]} {:range [7 21]}])

;; can remove the indexed ranges
(defn better-consolidate
  "Merge ranges until there are no overlaps"
  [ranges]
  (let [sorted-ranges (sort-ranges ranges)]
    (loop [remaining-ranges sorted-ranges
           consolidated-ranges []]
      (cond
        (empty? remaining-ranges)
        consolidated-ranges
        (= 1 (count remaining-ranges))
        (cons (first remaining-ranges) consolidated-ranges)
        :else
        (let [first-range (first remaining-ranges)
              second-range (second remaining-ranges)]
          (print remaining-ranges)
          (if (overlaps? first-range second-range)
            (recur (cons (simpler-merge-range first-range second-range) (drop 2 remaining-ranges)) consolidated-ranges)
            (recur (rest remaining-ranges) (cons first-range consolidated-ranges))))))))


(better-consolidate (map :ranges (parse-input real-input)))


(defn simpler-get-range-size [r]
  (print r)
  (inc (- (second r) (first r))))

(defn part2 [input]
  (let [ranges (map :ranges (parse-input input))
        non-overlapping-ranges (better-consolidate ranges)]
    (reduce + (map simpler-get-range-size non-overlapping-ranges))))

(part2 test-input)
(part2 real-input)
