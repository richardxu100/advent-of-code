(ns advent-of-code.day1.part2
  (:require [clojure.string :as str]))

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

(defn find-first-number
  "docstring"
  [string]
  (loop [current string]
    (cond
      (empty? current)
      nil
      (not (nil? (parse-number current)))
      (parse-number current)
      :else
      (recur (apply str (rest current))))))

(defn find-first-number-tail
  "docstring"
  [string]
  (loop [current string]
    (cond
      (empty? current)
      nil
      (not (nil? (parse-number current)))
      (parse-number current)
      :else
      (recur (apply str (butlast current))))))

(defn left-most-number-with-string-parsing
  "docstring"
  [string]
  (loop [remaining-string string
         current-string ""]
    (cond
      (Character/isDigit (first remaining-string))
      (- (int (first remaining-string)) (int \0))
      (not (nil? (find-first-number (str current-string (first remaining-string)))))
      (find-first-number (str current-string (first remaining-string)))
      :else
      (recur (rest remaining-string) (str current-string (first remaining-string))))))

(defn right-most-number-with-string-parsing
  "docstring"
  [string]
  (loop [remaining-string string
         current-string ""]
    (cond
      (Character/isDigit (last remaining-string))
      (- (int (last remaining-string)) (int \0))
      (not (nil? (find-first-number-tail (str (last remaining-string) current-string))))
      (find-first-number-tail (str (last remaining-string) current-string))
      :else
      (recur (butlast remaining-string) (str (last remaining-string) current-string )))))

(find-first-number-tail "twox")

(defn calc-calibration-value
  "docstring"
  [line]
  (+ (* 10 (left-most-number-with-string-parsing line) ) (right-most-number-with-string-parsing line)))

(defn calibration-total
  "docstring"
  [calibrations]
  (apply + (map calc-calibration-value calibrations)) )

(-> "./src/advent_of_code/calibration-values.txt"
    slurp
    (str/split #"\n")
    calibration-total)
