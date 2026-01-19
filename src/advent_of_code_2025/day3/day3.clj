(ns advent-of-code-2025.day3.day3
  (:require [clojure.string :as str]))

(def sample-input "./src/advent_of_code_2025/day3/sample_input.txt")
(def real-input "./src/advent_of_code_2025/day3/input.txt")


(defn max-voltage [s]
  (let [nums (map (comp parse-long str) s)
        left (apply max (drop-last 1 nums))
        left-index (.indexOf s (str left))
        right (apply max (drop (inc left-index) nums))]
    (parse-long (str left right))))

(.indexOf "8292" "9")
(max-voltage "8292")
(map parse-long "1232")

(defn part1 [input]
  (->> input
       slurp
       str/split-lines
       (map max-voltage)
       (reduce +)))

(part1 sample-input)
(part1 real-input)
