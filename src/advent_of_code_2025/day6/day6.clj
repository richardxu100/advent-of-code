(ns advent-of-code-2025.day6.day6 
  (:require [clojure.string :as str]))

(def input "./src/advent_of_code_2025/day6/input.txt")

(defn filter-blanks [input]
  (filter seq input))

(defn separate [input]
  (let [s (str/split input #" ")]
    (filter seq s)))

(def ex-row
  (-> "./src/advent_of_code_2025/day6/input.txt"
      slurp
      str/split-lines
      first
      (str/split #" ")
      filter-blanks
      ))



(->> "./src/advent_of_code_2025/day6/input.txt"
     slurp
     str/split-lines
     (map separate)
     first)

ex-row
(filter-blanks ex-row)

(filter seq (take 5 ex-row))

(defn parse-problems [input]
  (->> input
       slurp
       str/split-lines
       (map separate)
       (apply mapv vector)))

(take 4 (parse-problems input))

(mapv vector
      [:a :b] [1 2] [:yo :no])

(defn process-problem [problem]
  (let [args (map parse-long (drop-last problem))
        operation (last problem)]
    (case operation
      "+"
      (apply + args)
      "*"
      (apply * args))
    ))

(process-problem ["2" "3" "5" "+"])

(defn part1 [input]
  (let [problems (parse-problems input)]
    (reduce + (map process-problem problems))))

(part1 input)
