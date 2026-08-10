(ns advent-of-code-2025.day6.day6 
  (:require [clojure.string :as str]))

(def input "./src/advent_of_code_2025/day6/input.txt")
(def test-input "./src/advent_of_code_2025/day6/test_input.txt")

(defn filter-blanks [input]
  (filter seq input))

;; why can't i inline this in the threadding macro?
(defn separate [input]
  (let [s (str/split input #" ")]
    (filter seq s)))

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

(-> test-input
    slurp
    str/split-lines
    )

(def ex-line "*   +   *   +  ")
(def ex-line2 "*  ")

(defn find-slice-length [line]
  (loop [length-to-next-problem 1
         line' (rest line)]
    (if (or (not= \space (first line')) (empty? line'))
      length-to-next-problem
      (recur (inc length-to-next-problem) (rest line')))))

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

(defn fix-find-slice-length [lines]
  (println  "lines" lines)
  (->> lines
       drop-last
       (map next-num)
       (map num-digits)
       (apply max)))

(comment
  (find-slice-length ex-line)
  (find-slice-length ex-line2)

  (fix-find-slice-length ex-line))

(second ex-line)

(defn parse-problems-part2 [input]
  (let [lines (str/split-lines (slurp input))]
    (loop [lines' lines
           problems []]
      (if (empty? (first lines'))
        problems
        (let [slice-length (fix-find-slice-length lines')]
          (recur (map #(drop (inc slice-length) %) lines') (conj problems (map #(take slice-length %) lines'))))))))

(parse-problems-part2 test-input)

(fix-find-slice-length (str/split-lines (slurp test-input)))

(apply max '(3 2 1))
