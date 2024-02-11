(ns advent-of-code.day3.day3-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day3.day3 :as day3]))

(deftest it-returns-for-single-valid-number
  (is (= 4 (day3/sum-of-parts [["*4"]]))))

(deftest it-returns-for-other-single-valid-number
  (is (= 3 (day3/sum-of-parts [["*3"]]))))