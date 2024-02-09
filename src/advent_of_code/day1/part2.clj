(ns advent-of-code.day1.part2)

(defn convert-string-to-number
  "docstring"
  [string]
  (- (int string) (int \0)))

(defn left-most-number
  "docstring"
  [string]
  (loop [current string]
    (if (Character/isDigit (first current))
      (- (int (first current)) (int \0))
      (recur (rest current)))))

(defn calibration-total
  "docstring"
  [calibrations]
  (let [value (first calibrations)]
    (+ (* 10 (left-most-number value) ) (convert-string-to-number (last value)))))

