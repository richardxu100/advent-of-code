(ns advent-of-code.day5.day5-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day5.day5 :as day5]))

; parse which seeds need to be planted
; (destination range start) (source range start) (range length)

; so need to develop a mapping

(deftest it-returns-basic-target-mapping
  (is (= 25 (day5/calc-result [22] [{:destination-start 23 :source-start 20 :range 50}]))))