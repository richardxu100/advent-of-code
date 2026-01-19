(ns advent-of-code-2025.day2.day2
  (:require [clojure.string :as str]))

(defn valid-id? [n]
  (let [s (str n)]
    (if (odd? (count s))
      false
      (= (take (/ (count s) 2) s) (take-last (/ (count s) 2) s)))))

(comment
  (valid-id? 123124))

(def sample-input "./src/advent_of_code_2025/day2/sample_input.txt")
(def real-input "./src/advent_of_code_2025/day2/input.txt")

(defn convert-to-ranges [coll]
  (let [convert-to-range (fn [s]
                           (let [[left right] (str/split s #"-")]
                             (range (parse-long left) (inc (parse-long (str/trim right))))))]
    (map convert-to-range coll)))


(defn parse-ranges [input]
  (->
    input
    slurp
    (str/split #",")
    convert-to-ranges))

(defn part1 [input]
  (let [ranges (parse-ranges input)
        all-ids (flatten ranges)
        valid-ids (filter valid-id? all-ids)]
    (reduce + valid-ids)))

(comment
  (part1 sample-input)
  (part1 real-input)

  (parse-ranges sample-input)
  (parse-ranges real-input))
