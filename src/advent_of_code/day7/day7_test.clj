(ns advent-of-code.day7.day7-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day7.day7 :as day7]))

(deftest it-returns-high-card-for-distinct
  (is (= (day7/calc-hand-type "J2345") :high-card)))

(deftest it-returns-one-pair-for-pair
  (is (= (day7/calc-hand-type "JJ345") :one-pair)))

(deftest it-returns-two-pair-for-two-pair
  (is (= (day7/calc-hand-type "JJ335") :two-pair)))

(deftest it-returns-three-of-a-kind
  (is (= (day7/calc-hand-type "JJJ35") :three-of-a-kind)))

(deftest it-returns-full-house
  (is (= (day7/calc-hand-type "JJJ33") :full-house)))

(deftest it-returns-four-of-a-kind
  (is (= (day7/calc-hand-type "JJJJ3") :four-of-a-kind)))

(deftest it-returns-five-of-a-kind
  (is (= (day7/calc-hand-type "KKKKK") :five-of-a-kind)))

(deftest it-returns-basic-total-winnings
  (is (= (day7/calc-winnings [{:hand "J2345" :bid 10}]) 10)))

(deftest it-returns-total-winnings-for-two-hands
  (is (= (day7/calc-winnings [{:hand "J2345" :bid 10} {:hand "JJ234" :bid 30}]) 70)))

(deftest it-returns-total-winnings-for-three-hands
  (is (= (day7/calc-winnings [{:hand "J2345" :bid 10} {:hand "JJ234" :bid 30} {:hand "AAA34" :bid 50}]) 220)))

(deftest it-handles-basic-ties
  (is (= (day7/calc-winnings [{:hand "JJ245" :bid 10} {:hand "JJA34" :bid 50} {:hand "JJT34" :bid 30}]) 220)))

(deftest it-handles-more-complex-ties
  (is (= (day7/calc-winnings [{:hand "JJ245" :bid 10} {:hand "33A34" :bid 50} {:hand "AAT34" :bid 30}]) 160)))