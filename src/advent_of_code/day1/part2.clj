(ns advent-of-code.day1.part2
  (:require [clojure.string :as str]))

(defn left-most-number
  "docstring"
  [string]
  (loop [current string]
    (if (Character/isDigit (first current))
      (- (int (first current)) (int \0))
      (recur (rest current)))))

(defn right-most-number
  "docstring"
  [string]
  (left-most-number (reverse string)))

(defn calc-calibration-value
  "docstring"
  [line]
  (+ (* 10 (left-most-number line) ) (right-most-number line)))

(defn calibration-total
  "docstring"
  [calibrations]
  (apply + (map calc-calibration-value calibrations)) )


(-> "./src/advent_of_code/calibration-values.txt"
    slurp
    (str/split #"\n")
    calibration-total)
