(ns advent-of-code.day7.day7-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day7.day7 :as day7]))

(deftest it-returns-high-card-for-distinct
  (is (= (day7/calc-hand-type "12345") :high-card)))

(deftest it-returns-one-pair-for-pair
  (is (= (day7/calc-hand-type "11345") :one-pair)))

(deftest it-returns-two-pair-for-two-pair
  (is (= (day7/calc-hand-type "11335") :two-pair)))

(deftest it-returns-three-of-a-kind
  (is (= (day7/calc-hand-type "11135") :three-of-a-kind)))
