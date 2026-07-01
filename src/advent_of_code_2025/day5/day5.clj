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

(defn overlaps? [r1 r2]
  (let [[l1 r1] r1
        [l2 r2] r2]
    (or (>= r1 l2 l1) (>= r2 l1 l2))))

(comment
  (overlaps? [5 10] [12 23])
  (overlaps? [5 10] [8 23])
  (overlaps? [8 23] [5 10])
  (overlaps? [10 23] [5 10]))

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
(cannot-consolidate? [[1 2] [3 9]])

(defn find-any-overlapping-range [range remaining-ranges]
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

(merge-range {:range [10 23]} {:range [16 29]})

(defn remove-id [ranges id]
  (filter #(not (= (:id %) id)) ranges))

(remove-id [{:id 5 :range [10 23]} {:id 12 :range [10 25]}] 5)

(defn consolidate [indexed-ranges]
  (loop [current-range (first indexed-ranges)
         remaining-ranges (rest indexed-ranges)
         consolidated-ranges []]
    (cond
      (empty? remaining-ranges)
      (if (empty? current-range)
        consolidated-ranges
        (cons current-range consolidated-ranges))
      :else
      (let [overlapping-range (find-any-overlapping-range current-range remaining-ranges)]
        (if (nil? overlapping-range)
          (recur (first remaining-ranges) (rest remaining-ranges) (cons current-range consolidated-ranges))
          (let [consolidated-range (merge-range current-range overlapping-range)
                remaining-ranges' (remove-id remaining-ranges (:id overlapping-range))]
            (recur consolidated-range remaining-ranges' consolidated-ranges)))))))

;; probably need to use vectors. Also use debugger mode. And figure out how to create functions, rename things, other lsp things


(defn handle-add-range [non-overlapping-ranges range]
  (loop [ranges (conj non-overlapping-ranges range)]
    (if (cannot-consolidate? ranges)
      ranges
      (recur (consolidate ranges)))))


(defn convert-to-indexed-range [index s]
  (let [[l r] (str/split s #"-")]
    {:id index :range [(parse-long l) (parse-long r)]}))


(defn parse-ranges-part-2 [input]
  (let [lines (str/split-lines (slurp input))
        split-index (.indexOf lines "")]
    (map-indexed convert-to-indexed-range (take split-index lines))))

(parse-ranges-part-2 test-input)

(consolidate (parse-ranges-part-2 test-input))
(consolidate (parse-ranges-part-2 real-input))

(defn get-range-size [{r :range}]
  (inc (- (second r) (first r))))

(defn part2 [input]
  (let [indexed-ranges (parse-ranges-part-2 input)
        non-overlapping-ranges (consolidate indexed-ranges)]
    (reduce + (map get-range-size non-overlapping-ranges))))

(part2 test-input)

(part2 real-input)
