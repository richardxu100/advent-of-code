(ns advent-of-code-2025.day3.day3
  (:require [clojure.string :as str]))

(def sample-input "./src/advent_of_code_2025/day3/sample_input.txt")
(def real-input "./src/advent_of_code_2025/day3/input.txt")

(defn max-voltage
  "Find the max number in string s with length"
  [length s]
  (let [nums (map (comp parse-long str) s)]
    (loop [current-volts []
           remaining-nums nums]
      (if (= (count current-volts) length)
        (parse-long (apply str current-volts))
        (let [available-nums (drop-last (- length 1 (count current-volts)) remaining-nums)
              max-volt (apply max available-nums)
              max-volt-index (.indexOf remaining-nums max-volt)]
          (recur (conj current-volts max-volt) (drop (inc max-volt-index) remaining-nums)))))))

(defn part1 [input]
  (->> input
       slurp
       str/split-lines
       (map (partial max-voltage 2))
       (reduce +)))

(comment
  (part1 sample-input)
  (part1 real-input))

(defn part2 [input]
  (->> input
       slurp
       str/split-lines
       (map (partial max-voltage 12))
       (reduce +)))

(comment
  (part2 sample-input)
  (part2 real-input))

