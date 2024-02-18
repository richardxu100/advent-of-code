(ns advent-of-code.day-1
  (:require [clojure.string :as str]))

; how to read files in clojure
;slurp
;split by new line

(defn leftmostNumber
  "docstring"
  [string]
  (loop [current string]
    (if (Character/isDigit (first current))
      (- (int (first current)) (int \0))
      (recur (rest current)))))

(defn rightMostNumber
  "docstring"
  [string]
  (leftmostNumber (reverse string)))

(defn result
  "docstring"
  []
  (let [stringList (str/split (slurp "./src/advent_of_code/calibration-values.txt") #"\n")
        valuesForList (map #(+ (* 10 (leftmostNumber %)) (rightMostNumber %)) stringList)]
    (reduce + valuesForList)))


(defn result-with-threading
  "docstring"
  []
  (let [stringList (-> "./src/advent_of_code/calibration-values.txt"
                       (slurp)
                       (str/split #"\n"))]
    (->> stringList
         (map #(+ (* 10 (leftmostNumber %)) (rightMostNumber %)))
         (reduce +))))

(result-with-threading)

(defn part-2
  "docstring"
  [])

(defn parse-number
  "docstring"
  [string]
  (case string
    "zero" 0
    "one" 1
    "two" 2
    "three" 3
    "four" 4
    "five" 5
    "six" 6
    "seven" 7
    "eight" 8
    "nine" 9
    nil))

(defn parse-number-reverse
  "docstring"
  [string]
  (case string
    "orez" 0
    "eno" 1
    "owt" 2
    "eerht" 3
    "ruof" 4
    "evif" 5
    "xis" 6
    "neves" 7
    "thgie" 8
    "enin" 9
    nil))

(defn leftmost-number-including-string-with-parse-number
  "docstring"
  [string, parse-number-impl]
  (loop [remaining-string string
         current-string ""]
    (cond
      (Character/isDigit (first remaining-string))
      (- (int (first remaining-string)) (int \0))
      (not (nil? (parse-number-impl (str current-string (first remaining-string)))))
      (parse-number (str current-string (first remaining-string)))
      :else
      (recur (rest remaining-string) (str current-string (first remaining-string))))))

(defn leftmost-number-including-string
  "docstring"
  [string]
  (leftmost-number-including-string-with-parse-number string parse-number))

(defn rightmost-number-including-string
  "docstring"
  [string]
  (leftmost-number-including-string-with-parse-number (apply str (reverse string)) parse-number-reverse))

(leftmost-number-including-string "xtwone3four")

(defn part2-result-with-threading
  "docstring"
  []
  (let [stringList (-> "./src/advent_of_code/calibration-values.txt"
                       (slurp)
                       (str/split #"\n"))]
    (->> stringList
         (map #(+ (* 10 (leftmost-number-including-string %)) (rightmost-number-including-string %)))
         (reduce +))))

(part2-result-with-threading)

(apply str (reverse "three"))
;(parse-number-reverse )