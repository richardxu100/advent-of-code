(ns advent-of-code.day1.part2)

(defn convert-string-to-number
  "docstring"
  [string]
  (- (int string) (int \0)))

(defn calibration-total
  "docstring"
  [calibrations]
  (let [value (first calibrations)]
    (+ (* 10 (convert-string-to-number (first value)) ) (convert-string-to-number (last value)))))

