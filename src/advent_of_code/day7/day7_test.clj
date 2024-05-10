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

(deftest it-returns-full-house
  (is (= (day7/calc-hand-type "11133") :full-house)))

(deftest it-returns-four-of-a-kind
  (is (= (day7/calc-hand-type "11113") :four-of-a-kind)))

(deftest it-returns-five-of-a-kind
  (is (= (day7/calc-hand-type "KKKKK") :five-of-a-kind)))
