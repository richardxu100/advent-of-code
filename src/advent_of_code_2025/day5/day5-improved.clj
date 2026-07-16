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

;; Part 2

(defn overlaps? [r1 r2]
  (let [[l1 r1] r1
        [l2 r2] r2]
    (or (>= r1 l2 l1) (>= r2 l1 l2))))

(defn merge-range [r1 r2]
  [(min (first r1) (first r2))
    (max (second r1) (second r2))])

(defn sort-consolidate
  "Merge ranges until there are no overlaps"
  [ranges]
  (let [sorted-ranges (sort-by first ranges)]
    (loop [remaining-ranges sorted-ranges
           consolidated-ranges []]
      (cond
        (= 1 (count remaining-ranges))
        (cons (first remaining-ranges) consolidated-ranges)
        :else
        (let [first-range (first remaining-ranges)
              second-range (second remaining-ranges)]
          (if (overlaps? first-range second-range)
            (recur (cons (merge-range first-range second-range) (drop 2 remaining-ranges)) consolidated-ranges)
            (recur (rest remaining-ranges) (cons first-range consolidated-ranges))))))))

(defn consolidate
  [ranges]
  (let [sorted-ranges (sort-by first ranges)]
    (reduce (fn [result range]
              (if (empty? result)
                [range]
                (if (overlaps? (last result) range)
                  (conj (vec (drop-last result)) (merge-range (last result) range)) ;; I need to convert drop-last to vec, as conj on a () list adds items to the front, not the end
                  (conj result range)))) [] sorted-ranges)))

(comment
  (sort-consolidate (:ranges (parse-input test-input)))
  (consolidate sort-consolidate)
  (consolidate (:ranges (parse-input real-input)))
  (parse-input test-input))

(defn get-range-size [r]
  (print r)
  (inc (- (second r) (first r))))

(defn part2 [input]
  (let [ranges (:ranges (parse-input input))
        non-overlapping-ranges (consolidate ranges)]
    (reduce + (map get-range-size non-overlapping-ranges))))

(part2 test-input)
(part2 real-input)
