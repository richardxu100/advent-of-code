(ns advent-of-code-2025.day6.day6 
  (:require [clojure.string :as str]))

(def input "./src/advent_of_code_2025/day6/input.txt")
(def test-input "./src/advent_of_code_2025/day6/test_input.txt")

(defn separate [input]
  (let [s (str/split input #" ")]
    (filter seq s)))

(defn parse-problems [input]
  (->> input
       slurp
       str/split-lines
       (map separate)
       (apply mapv vector))) ;; this transposes the vector

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

(def ex-line "*   +   *   +  ")

(defn next-num [line]
  (println "line: " line)
  (-> line
      str/join
      (str/split #" ")
      (as-> v (filter seq v)) ;; a way to choose where to inject into the pipeline
      first
      parse-long))

(next-num '(\space \space \2 \3 \space \5))

(defn num-digits [n]
  (count (str n)))

(num-digits 3212)

(defn find-slice-length
  "Equals the number of digits of the next largest number"
  [lines]
  (->> lines
       drop-last
       (map next-num)
       (map num-digits)
       (apply max)))

(comment
  (find-slice-length ex-line)
  (second ex-line))

(defn parse-problems-part2 [input]
  (let [lines (str/split-lines (slurp input))]
    (loop [lines' lines
           problems []]
      (if (empty? (first lines'))
        problems
        (let [slice-length (find-slice-length lines')]
          (recur (map #(drop (inc slice-length) %) lines')
                 (conj problems (map #(take slice-length %) lines'))))))))

(def ex-problem
  (first (parse-problems-part2 test-input)))

(defn parce-args-part2 [problem]
  (->> problem
       drop-last
       (apply map str)
       (map str/trim)
       (map parse-long)))

(comment
  (find-slice-length (str/split-lines (slurp test-input)))
  (apply max '(3 2 1)))

(defn process-problem-part2 [problem]
  (let [args (parce-args-part2 problem)
        operation (first (last problem))]
    (case operation
      \+
      (apply + args)
      \*
      (apply * args))))

(process-problem-part2 ex-problem)

(defn part2 [input]
  (let [problems (parse-problems-part2 input)]
    (reduce + (map process-problem-part2 problems))))

(part2 input)
