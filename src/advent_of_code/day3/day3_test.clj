(ns advent-of-code.day3.day3-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day3.day3 :as day3]))

(deftest it-returns-for-single-valid-number
  (is (= 4 (day3/sum-of-parts [["*" "4"]]))))

(deftest it-returns-for-other-single-valid-number
  (is (= 3 (day3/sum-of-parts [["*" "3"]]))))

(deftest it-returns-for-bigger-number
  (is (= 34 (day3/sum-of-parts [["*" "3" "4"]]))))

(deftest it-returns-0-if-no-neighbor-is-symbol
  (is (= 0 (day3/sum-of-parts [["." "3"]]))))

(deftest it-returns-0-if-no-neighbor-is-symbol-bigger-number
  (is (= 0 (day3/sum-of-parts [["." "3" "4"]]))))

(deftest it-returns-0-if-no-neighbor-is-symbol-only-numbers
  (is (= 0 (day3/sum-of-parts [["3" "4"]]))))

(deftest it-returns-number-if-right-neighbor-is-symbol
  (is (= 34 (day3/sum-of-parts [["3" "4" "*"]]))))

(deftest it-returns-sum-of-multiple-numbers
  (is (= 79 (day3/sum-of-parts [["3" "4" "*" "4" "5"]]))))

(deftest it-returns-sum-of-three-numbers
  (is (= 130 (day3/sum-of-parts [["3" "4" "*" "4" "5" "*" "5" "1"]]))))

(deftest it-returns-only-valid-numbers
  (is (= (day3/sum-of-parts [["*" "3" "4" "*" "4" "5" "." "51"]]) 79)))