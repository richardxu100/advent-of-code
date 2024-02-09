(ns advent-of-code.day1.part2-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day1.part2 :as part2]))

(deftest it-calculates-for-number-based-string
  (is (= 15 (part2/calibration-total ["15"]))))

(deftest it-calculates-for-number-based-string-without-number-at-the-front
  (is (= 15 (part2/calibration-total ["apple15"]))))

(deftest it-calculates-for-number-based-string-without-number-at-the-end
  (is (= 15 (part2/calibration-total ["15apple"]))))

