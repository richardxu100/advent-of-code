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

(defn part2-max-voltage [s length]
  (let [nums (map (comp parse-long str) s)]
    (loop [current-volts []
           remaining-nums nums]
      (if (= (count current-volts) length)
        (parse-long (apply str current-volts))
        (let [available-nums (drop-last (- length 1 (count current-volts)) remaining-nums)
              max-volt (apply max available-nums)
              max-volt-index (.indexOf remaining-nums max-volt)]
          (recur (conj current-volts max-volt) (drop (inc max-volt-index) remaining-nums)))))))

(comment
  (part2-max-voltage "13746892" 4)
  (part2-max-voltage "987654321111111" 12)
  (part2-max-voltage "234234234234278" 12))

(comment
  (.indexOf "8292" "9")
  (max-voltage "8292")
  (map parse-long "1232"))

(defn part1 [input]
  (->> input
       slurp
       str/split-lines
       (map max-voltage)
       (reduce +)))

(comment
  (part1 sample-input)
  (part1 real-input))


(defn part2 [input]
  (->> input
       slurp
       str/split-lines
       (map (fn [s] (part2-max-voltage s 12)))
       (reduce +)))

(comment
  (part2 sample-input)
  (part2 real-input))

